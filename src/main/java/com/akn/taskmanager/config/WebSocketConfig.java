package com.akn.taskmanager.config;

import com.akn.taskmanager.service.JWTTokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private JWTTokenService jwtTokenService;

    public WebSocketConfig(JWTTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/akn-chat")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/exchange", "/topic")
                .setRelayHost("192.168.56.102")
                .setRelayPort(61613)
                .setClientPasscode("guest")
                .setClientLogin("guest");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("Authorization");
                    boolean isEmpty = CollectionUtils.isEmpty(authorization);
                    if (!isEmpty) {
                        try {
                            String username = jwtTokenService.validateAccessToken(authorization.get(0));
                            Optional.ofNullable(username).orElseThrow(() -> new RuntimeException("User not found"));
                            Authentication user = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                            accessor.setUser(user);
                        } catch (MessageDeliveryException exception) {
                            accessor.setHeader("status", "403");
                            throw new RuntimeException("User session expired");
                        }

                    } else {
                        throw new RuntimeException("Authorization header not present");
                    }

                }
                return message;
            }
        });
    }
}

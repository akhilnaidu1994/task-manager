package com.akn.taskmanager.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTTokenService {

    @Value("${token_secret}")
    private String tokenSecret;

    @Value("${refresh_token_secret}")
    private String refreshTokenSecret;

    @Value("${token_expiration_time}")
    private long tokenExpirationTime;

    @Value("${refresh_token_expiration_time}")
    private long refreshTokenExpirationTime;

    @Value("${header_string}")
    private String headerString;

    @Value("${token_prefix}")
    private String tokenPrefix;


    public JWTTokenService() {
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getRefreshTokenSecret() {
        return refreshTokenSecret;
    }

    public void setRefreshTokenSecret(String refreshTokenSecret) {
        this.refreshTokenSecret = refreshTokenSecret;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }


    public long getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public long getRefreshTokenExpirationTime() {
        return refreshTokenExpirationTime;
    }

    public String getHeaderString() {
        return headerString;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public void setTokenExpirationTime(long tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public void setRefreshTokenExpirationTime(long refreshTokenExpirationTime) {
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public void setHeaderString(String headerString) {
        this.headerString = headerString;
    }

    public String generateAccessToken(String username){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .sign(Algorithm.HMAC512(tokenSecret.getBytes()));
    }

    public String generateRefreshToken(String username){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .sign(Algorithm.HMAC512(refreshTokenSecret.getBytes()));
    }

    public String validateAccessToken(String token){
        return JWT.require(Algorithm.HMAC512(getTokenSecret().getBytes()))
                .build()
                .verify(token.replace(getTokenPrefix(), ""))
                .getSubject();
    }

    public String validateRefreshToken(String token){
        return JWT.require(Algorithm.HMAC512(getRefreshTokenSecret().getBytes()))
                .build()
                .verify(token.replace(getTokenPrefix(), ""))
                .getSubject();
    }
}

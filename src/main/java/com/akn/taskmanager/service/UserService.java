package com.akn.taskmanager.service;

import com.akn.taskmanager.model.User;
import com.akn.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User register(User user){
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return this.userRepository.save(user);
    }

    public  User getUser(User user) {
        return this.userRepository.findByEmail(user.getEmail());
    }

}

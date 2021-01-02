package com.akn.taskmanager.service;

import com.akn.taskmanager.model.User;
import com.akn.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<User> searchUser(String searchText) {
        return userRepository.searchUsers(searchText);
    }

    public  User getUser(String username) {
        return this.userRepository.findByEmail(username);
    }

    public User getUserById(String id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}

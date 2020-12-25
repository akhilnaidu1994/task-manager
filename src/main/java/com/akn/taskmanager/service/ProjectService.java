package com.akn.taskmanager.service;

import com.akn.taskmanager.model.Project;
import com.akn.taskmanager.model.User;
import com.akn.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private UserRepository userRepository;

    public ProjectService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Project> getAllProjects(String userid){
        Optional<User> userById = userRepository.findById(userid);
        User user = userById.orElseThrow(() -> new RuntimeException("User not found"));
        return user.getProjects();
    }

    public Project editProject(String userid, Project project){
        return null;
    }


    public Project addProject(){
        return null;
    }

    public Project deleteProject(){
        return null;
    }
}

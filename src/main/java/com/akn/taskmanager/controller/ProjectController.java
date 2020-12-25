package com.akn.taskmanager.controller;

import com.akn.taskmanager.model.Project;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @GetMapping
    public List<Project> getAllProjects(){
        return null;
    }

    @PutMapping("/{id}")
    public Project editProject(@PathVariable("id") String id){
        return null;
    }

    @PostMapping
    public Project addProject(){
        return null;
    }

    @DeleteMapping
    public Project deleteProject(){
        return null;
    }
}

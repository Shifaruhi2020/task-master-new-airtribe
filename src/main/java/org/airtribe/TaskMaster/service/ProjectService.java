package org.airtribe.TaskMaster.service;

import java.util.List;

import org.airtribe.TaskMaster.entity.Project;
import org.airtribe.TaskMaster.entity.User;
import org.airtribe.TaskMaster.repository.ProjectRepository;
import org.airtribe.TaskMaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

     @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Project createProject(Project project, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        project.setCreatedBy(user);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public void inviteMember(Long projectId, Long userId) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new RuntimeException("Project not found"));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    project.getTeamMembers().add(user);
    projectRepository.save(project);
}

}

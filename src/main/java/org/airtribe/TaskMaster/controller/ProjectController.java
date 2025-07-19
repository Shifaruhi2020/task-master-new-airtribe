package org.airtribe.TaskMaster.controller;

import java.util.List;
import java.util.Set;

import org.airtribe.TaskMaster.entity.Project;
import org.airtribe.TaskMaster.entity.User;
import org.airtribe.TaskMaster.service.ProjectService;
import org.airtribe.TaskMaster.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ProjectController {
     @Autowired
    private ProjectService projectService;

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Missing Authorization token");
        }
        return JwtUtil.getUserIdFromToken(token);
    }

    @PostMapping("/project/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(projectService.createProject(project, userId));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @PostMapping("/project/{projectId}/invite/{userId}")
    public ResponseEntity<String> inviteMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectService.inviteMember(projectId, userId);
        return ResponseEntity.ok("User invited to project successfully.");
    }

    @GetMapping("/project/{projectId}/members")
    public ResponseEntity<Set<User>> getProjectMembers(@PathVariable Long projectId) {
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project.getTeamMembers());
    }

}

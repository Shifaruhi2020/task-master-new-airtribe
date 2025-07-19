package org.airtribe.TaskMaster.controller;

import java.util.List;

import org.airtribe.TaskMaster.entity.Task;
import org.airtribe.TaskMaster.service.TaskService;
import org.airtribe.TaskMaster.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
// This controller will handle task-related operations
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/tasks/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        Task createdTask = taskService.createTask(task, userId);
        return ResponseEntity.ok(createdTask);
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
    if (token == null || token.isEmpty()) {
        throw new RuntimeException("Missing Authorization token");
    }
    return JwtUtil.getUserIdFromToken(token);
    }

     // Get tasks assigned to current user
    @GetMapping("/tasks/my-tasks")
    public ResponseEntity<List<Task>> getMyTasks(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        List<Task> tasks = taskService.getTasksForUser(userId);
        return ResponseEntity.ok(tasks);
    }

    // Mark a task as completed
    @PutMapping("/tasks/{taskId}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long taskId, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        Task updatedTask = taskService.markTaskAsCompleted(taskId, userId);
        return ResponseEntity.ok(updatedTask);
    }

    // Assign a task to another user
    @PutMapping("/tasks/{taskId}/assign/{assigneeId}")
    public ResponseEntity<Task> assignTask(
            @PathVariable Long taskId,
            @PathVariable Long assigneeId,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        Task updatedTask = taskService.assignTaskToUser(taskId, assigneeId, userId);
        return ResponseEntity.ok(updatedTask);
    }

    // Filter tasks by status (OPEN or COMPLETED)
    @GetMapping("/tasks/filter")
    public ResponseEntity<List<Task>> filterTasksByStatus(
            @RequestParam Task.Status status,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        List<Task> tasks = taskService.filterTasksByStatus(status, userId);
        return ResponseEntity.ok(tasks);
    }

    // Search tasks by title or description
    @GetMapping("/tasks/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam String query,
            HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        List<Task> tasks = taskService.searchTasks(query, userId);
        return ResponseEntity.ok(tasks);
    }

   

    
}

package org.airtribe.TaskMaster.service;

import org.airtribe.TaskMaster.entity.Project;
import org.airtribe.TaskMaster.entity.Task;
import org.airtribe.TaskMaster.entity.User;
import org.airtribe.TaskMaster.repository.ProjectRepository;
import org.airtribe.TaskMaster.repository.TaskRepository;
import org.airtribe.TaskMaster.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // Create a new task
    public Task createTask(Task task, Long createdByUserId) {
        User createdBy = getUserById(createdByUserId);
        task.setCreatedBy(createdBy);

        // Optional: default assign to self if assignedTo is null
        if (task.getAssignedTo() == null) {
            task.setAssignedTo(createdBy);
        }

         // ✅ Fetch full project from DB if project ID is provided
        if (task.getProject() != null && task.getProject().getId() != null) {
            Project project = projectRepository.findById(task.getProject().getId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            task.setProject(project);
        }

        return taskRepository.save(task);
    }

    // Get tasks assigned to the current user
    public List<Task> getTasksForUser(Long userId) {
        User user = getUserById(userId);
        return taskRepository.findByAssignedTo(user);
    }

    // Mark a task as completed (only if it's assigned to this user)
    public Task markTaskAsCompleted(Long taskId, Long userId) {
        Task task = getTaskById(taskId);

        if (!task.getAssignedTo().getId().equals(userId)) {
            throw new RuntimeException("You are not assigned to this task");
        }

        task.setStatus(Task.Status.COMPLETED);
        return taskRepository.save(task);
    }

    // Assign task to another user
    public Task assignTaskToUser(Long taskId, Long assigneeId, Long assignerId) {
        Task task = getTaskById(taskId);
        User assignee = getUserById(assigneeId);
        User assigner = getUserById(assignerId);

        // Optional: check if assigner is the creator
        if (!task.getCreatedBy().getId().equals(assignerId)) {
            throw new RuntimeException("Only the creator can assign the task");
        }

        task.setAssignedTo(assignee);
        return taskRepository.save(task);
    }

    // Filter tasks by status for the logged-in user
    public List<Task> filterTasksByStatus(Task.Status status, Long userId) {
        User user = getUserById(userId);
        return taskRepository.findByAssignedToAndStatus(user, status);
    }

    // Search tasks by title or description (only assigned to this user)
    public List<Task> searchTasks(String query, Long userId) {
        User user = getUserById(userId);
        return taskRepository.searchUserTasks(query, user);
    }

    // Utility methods
    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
}

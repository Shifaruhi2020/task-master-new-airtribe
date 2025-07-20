package org.airtribe.TaskMaster.service;

import java.util.List;

import org.airtribe.TaskMaster.entity.Comment;
import org.airtribe.TaskMaster.entity.Task;
import org.airtribe.TaskMaster.entity.User;
import org.airtribe.TaskMaster.repository.CommentRepository;
import org.airtribe.TaskMaster.repository.TaskRepository;
import org.airtribe.TaskMaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Comment addComment(Long taskId, String content, Long userId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setCreatedBy(user);
        comment.setContent(content);

        return commentRepository.save(comment);
    }

        public List<Comment> getCommentsForTask(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
    }

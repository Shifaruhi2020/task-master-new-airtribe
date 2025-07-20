package org.airtribe.TaskMaster.controller;


import java.util.List;
import java.util.Map;

import org.airtribe.TaskMaster.entity.Comment;
import org.airtribe.TaskMaster.service.CommentService;
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
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("tasks/{taskId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long taskId, 
                                              @RequestBody Map<String, String> body,
                                              HttpServletRequest request) {
        String content = body.get("content");
        Long userId = JwtUtil.getUserIdFromToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(commentService.addComment(taskId, content, userId));
    }
    
    @GetMapping("tasks/{taskId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsForTask(taskId));
    }

}

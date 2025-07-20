package org.airtribe.TaskMaster.controller;

import java.io.IOException;
import java.util.List;

import org.airtribe.TaskMaster.entity.Attachment;
import org.airtribe.TaskMaster.service.AttachmentService;
import org.airtribe.TaskMaster.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AttachmentController {

     @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<Attachment> uploadFile(@PathVariable Long taskId,
                                                 @RequestParam("file") MultipartFile file,
                                                 HttpServletRequest request) throws IOException {
        Long userId = JwtUtil.getUserIdFromToken(request.getHeader("Authorization"));
        return ResponseEntity.ok(attachmentService.saveAttachment(taskId, file, userId));
    }

    @GetMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<List<Attachment>> getFiles(@PathVariable Long taskId) {
        return ResponseEntity.ok(attachmentService.getAttachments(taskId));
    }
}

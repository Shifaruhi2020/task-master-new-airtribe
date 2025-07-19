package org.airtribe.TaskMaster.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.airtribe.TaskMaster.entity.Attachment;
import org.airtribe.TaskMaster.entity.Task;
import org.airtribe.TaskMaster.repository.AttachmentRepository;
import org.airtribe.TaskMaster.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachmentService {

   private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Attachment saveAttachment(Long taskId, MultipartFile file, Long userId) throws IOException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Create uploads directory if it doesn’t exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String savedFileName = UUID.randomUUID() + "_" + originalFilename;

        File destination = new File(UPLOAD_DIR + savedFileName);
        file.transferTo(destination); // <-- This was failing earlier

        Attachment attachment = new Attachment();
        attachment.setTask(task);
        attachment.setFilename(savedFileName);
        attachment.setFilePath(destination.getAbsolutePath());

        return attachmentRepository.save(attachment);
    }

    public List<Attachment> getAttachments(Long taskId) {
    Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
    return attachmentRepository.findByTaskId(task.getId());
    }

}

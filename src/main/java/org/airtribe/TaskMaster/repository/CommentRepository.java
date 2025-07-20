package org.airtribe.TaskMaster.repository;

import java.util.List;

import org.airtribe.TaskMaster.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);

}

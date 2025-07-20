package org.airtribe.TaskMaster.repository;

import org.airtribe.TaskMaster.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}

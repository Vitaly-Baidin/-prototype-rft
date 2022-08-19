package com.render.server.repository;

import com.render.server.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Boolean existsByName(String name);

    Optional<Task> findByName(String name);
}

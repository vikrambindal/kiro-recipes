package com.kirotodo.adapter.out.database;

import com.kirotodo.adapter.out.database.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TodoJpaRepository extends JpaRepository<TodoEntity, UUID> {
}
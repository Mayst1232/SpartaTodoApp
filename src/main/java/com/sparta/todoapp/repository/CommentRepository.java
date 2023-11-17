package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCard_Id(Long id);
}

package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCardId(Long id);

    List<Comment> findAllByUser(User user);
}

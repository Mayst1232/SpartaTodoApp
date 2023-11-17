package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCard_Id(Long id);

    Optional<Comment> findByUserAndId(User user, Long commentId);
}

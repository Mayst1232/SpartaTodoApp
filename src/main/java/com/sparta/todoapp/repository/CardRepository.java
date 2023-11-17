package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByUser(User user);
}

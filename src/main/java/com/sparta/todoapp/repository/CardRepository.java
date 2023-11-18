package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByUser(User user);

    List<Card> findAllByVisible(boolean b);

    Optional<Card> findByUserAndId(User user, Long id);

    List<Card> findAllByTitle(String title);
}

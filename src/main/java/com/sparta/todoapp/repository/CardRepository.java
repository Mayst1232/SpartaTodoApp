package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}

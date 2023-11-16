package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.CardCompleteRequestDto;
import com.sparta.todoapp.dto.CardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "card")
@NoArgsConstructor
public class Card extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean complete;

    public Card(CardRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.complete = requestDto.isComplete();
    }

    public void completeUpdate(CardCompleteRequestDto requestDto) {
        this.complete = requestDto.isComplete();
    }

}

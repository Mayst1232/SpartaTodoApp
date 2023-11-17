package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.CardCompleteRequestDto;
import com.sparta.todoapp.dto.CardModifyRequestDto;
import com.sparta.todoapp.dto.CardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "card" ,fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    public Card(CardRequestDto requestDto, User user){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.complete = requestDto.isComplete();
        this.user = user;
    }

    public void completeUpdate(CardCompleteRequestDto requestDto) {
        this.complete = requestDto.isComplete();
    }

    public void updateCard(CardModifyRequestDto requestDto) {
        if(requestDto.getTitle() == null && requestDto.getContent() != null) {
            this.content = requestDto.getContent();
        } else if(requestDto.getTitle() != null && requestDto.getContent() == null) {
            this.title = requestDto.getTitle();
        } else if(requestDto.getContent() != null && requestDto.getTitle() != null){
            this.title = requestDto.getTitle();
            this.content = requestDto.getContent();
        }
    }
}
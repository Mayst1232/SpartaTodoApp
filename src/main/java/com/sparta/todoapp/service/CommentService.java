package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;


    public CommentResponseDto createComments(Long cardId, CommentRequestDto requestDto, User user) {
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new NullPointerException("해당 카드는 존재하지 않습니다."));
        Comment comment = commentRepository.save(new Comment(requestDto.getContent(),user.getUsername(), user, card));
        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getComments(Long id) {
        List<Comment> commentList = commentRepository.findAllByCard_Id(id);
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            responseDtoList.add(new CommentResponseDto(comment));
        }

        return responseDtoList;
    }

    @Transactional
    public CommentResponseDto modifyComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = checkComment(commentId, user);
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = checkComment(commentId, user);
        commentRepository.delete(comment);
    }

    public Comment checkComment(Long commentId, User user){
        Comment myComment = new Comment();

        List<Comment> commentList = commentRepository.findAllByUser(user);

        if(commentList.isEmpty()){
            throw new NullPointerException("작성한 댓글이 존재하지 않습니다.");
        }

        Comment checkComment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 id의 댓글은 존재하지 않습니다.")
        );

        for (Comment comment : commentList) {
            if(comment.getId().equals(commentId)){
                myComment = comment;
                break;
            }
        }

        if(myComment.getId() == null){
            throw new IllegalArgumentException("댓글을 작성한 사람만 수정/삭제 할 수 있습니다.");
        }

        return myComment;
    }
}

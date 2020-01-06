package net.atos.service;


import net.atos.model.Car;
import net.atos.model.Comment;
import net.atos.model.dto.CommentDto;
import net.atos.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(CommentDto messageDto) {
        Comment message = new Comment(messageDto.getAuthor(),messageDto.getCar(),messageDto.getContent());
        commentRepository.save(message);
        return message;
    }

    public List <Comment> getCommentsAssignedToCar(Car car) {
        return commentRepository.findAllByCar(car).stream()
                                                  .sorted((o1, o2) -> o2.getComment_id().compareTo(o1.getComment_id()))
                                                  .collect(Collectors.toList());
    }

}

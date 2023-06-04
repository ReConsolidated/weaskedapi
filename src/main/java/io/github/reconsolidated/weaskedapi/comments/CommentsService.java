package io.github.reconsolidated.weaskedapi.comments;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;

    public Comment addComment(Comment comment) {
        return commentsRepository.save(comment);
    }

    public List<Comment> getAll() {
        return commentsRepository.findAll();
    }

    public List<Comment> getAllByCode(String code) {
        return commentsRepository.findAllByCode(code);
    }

    public void deleteAll() {
        commentsRepository.deleteAll();
    }
}

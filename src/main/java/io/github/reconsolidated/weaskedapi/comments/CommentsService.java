package io.github.reconsolidated.weaskedapi.comments;

import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUser;
import io.github.reconsolidated.weaskedapi.reactions.Reaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Comment addReaction(AppUser user, Long commentId, String reactionType) {
        Comment comment = commentsRepository.findById(commentId).orElseThrow();
        if (comment.getReactions().stream().anyMatch(
                (c) -> c.getAuthorId().equals(user.getId()) &&
                        c.getReactionType().equals(reactionType))){
            throw new IllegalArgumentException("This user has already used this reaction on this comment");
        }

        Reaction reaction = new Reaction();
        reaction.setCommentId(commentId);
        reaction.setReactionType(reactionType);
        reaction.setAuthorId(user.getId());
        reaction.setCreatedAt(Date.valueOf(LocalDate.now()));
        comment.getReactions().add(reaction);

        return commentsRepository.save(comment);
    }

    public Comment removeReaction(AppUser user, Long commentId, String reactionType) {
        Comment comment = commentsRepository.findById(commentId).orElseThrow();

        comment.getReactions().removeIf((c) -> c.getAuthorId().equals(user.getId()) &&
                c.getReactionType().equals(reactionType));

        return commentsRepository.save(comment);
    }
}

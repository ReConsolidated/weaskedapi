package io.github.reconsolidated.weaskedapi.comments;

import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUser;
import io.github.reconsolidated.weaskedapi.reactions.Reaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentsService {
    private final SiteCommentsRepository siteCommentsRepository;

    public Comment addComment(String code, Comment comment) {
        SiteComments siteComments = siteCommentsRepository.findById(code).orElse(new SiteComments(code));
        siteComments.getComments().add(comment);
        siteCommentsRepository.save(siteComments);
        return comment;
    }

    public List<Comment> getAllByCode(String code) {
        var opt = siteCommentsRepository.findById(code);
        if (opt.isEmpty()) {
            return new ArrayList<>();
        }
        return opt.get().getComments();
    }

    public void deleteAll() {
        // siteCommentsRepository.deleteAll();
    }

    public Comment updateReaction(AppUser user, String code, String commentId, String reactionType) {
        SiteComments siteComments = siteCommentsRepository.findById(code).orElseThrow();
        Comment comment = siteComments.getComments().stream()
                .filter((c) -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow();
        if (comment.getReactions().stream().anyMatch(
                (c) -> c.getAuthorId().equals(user.getId()) &&
                        c.getReactionType().equals(reactionType))){
            return removeReaction(user, code, commentId, reactionType);
        }


        Reaction reaction = new Reaction();
        reaction.setReactionType(reactionType);
        reaction.setAuthorId(user.getId());
        reaction.setCreatedAt(System.currentTimeMillis());
        comment.getReactions().add(reaction);
        siteCommentsRepository.save(siteComments);
        return comment;
    }

    public Comment removeReaction(AppUser user, String code, String commentId, String reactionType) {
        SiteComments siteComments = siteCommentsRepository.findById(code).orElseThrow();
        Comment comment = siteComments.getComments().stream()
                .filter((c) -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow();
        for (Reaction reaction : comment.getReactions()) {
            if (reaction.getAuthorId().equals(user.getId()) && reaction.getReactionType().equals(reactionType)) {
                comment.getReactions().remove(reaction);
                break;
            }
        }
        siteCommentsRepository.save(siteComments);
        return comment;
    }

    public Comment addSubComment(String code, String commentId, Comment comment) {
        SiteComments siteComments = siteCommentsRepository.findById(code).orElseThrow();
        Comment parentComment = siteComments.getComments().stream()
                .filter((c) -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow();
        parentComment.getSubComments().add(comment);
        siteCommentsRepository.save(siteComments);
        return comment;
    }

    public Comment updateComment(AppUser currentUser, String code, String commentId, String newText) {
        SiteComments siteComments = siteCommentsRepository.findById(code).orElseThrow();
        Comment comment = siteComments.getComments().stream()
                .filter((c) -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow();
        if (!comment.getAuthorId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not the author of this comment");
        }
        comment.setText(newText);
        comment.setWasEdited(true);
        siteCommentsRepository.save(siteComments);
        return comment;
    }

    public Comment deleteComment(AppUser currentUser, String code, String commentId) {
        SiteComments siteComments = siteCommentsRepository.findById(code).orElseThrow();
        Comment comment = siteComments.getComments().stream()
                .filter((c) -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow();
        if (!comment.getAuthorId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not the author of this comment");
        }
        siteComments.getComments().remove(comment);
        siteCommentsRepository.save(siteComments);
        return comment;
    }
}

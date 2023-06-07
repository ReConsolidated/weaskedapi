package io.github.reconsolidated.weaskedapi.comments;

import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUser;
import io.github.reconsolidated.weaskedapi.authentication.currentUser.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
public class CommentsController {
    private final CommentsService commentsService;

    @GetMapping("/comments/{code}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable(name="code") String code) {
        return ResponseEntity.ok(commentsService.getAllByCode(code));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<Comment> addReaction(@CurrentUser AppUser user,
                                                     @PathVariable(name="commentId") Long commentId,
                                                     @RequestParam String reaction) {
        return ResponseEntity.ok(commentsService.updateReaction(user, commentId, reaction));
    }

    @PatchMapping("/comments/{commentId}/remove-reaction")
    public ResponseEntity<Comment> removeReaction(@CurrentUser AppUser user,
                                               @PathVariable(name="commentId") Long commentId,
                                               @RequestParam String reaction) {
        return ResponseEntity.ok(commentsService.removeReaction(user, commentId, reaction));
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@CurrentUser AppUser user, @RequestBody Comment comment) {
        comment.setAuthorId(user.getId());
        comment.setAuthorName(user.getUserName());
        comment.setCreatedAt(Date.valueOf(LocalDate.now()));
        comment.setReactions(new ArrayList<>());
        Comment result = commentsService.addComment(comment);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/clear_all")
    public ResponseEntity<?> clearAll() {
        commentsService.deleteAll();
        return ResponseEntity.ok().build();
    }
}

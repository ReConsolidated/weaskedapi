package io.github.reconsolidated.weaskedapi.comments;

import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUser;
import io.github.reconsolidated.weaskedapi.authentication.currentUser.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("{code}")
public class CommentsController {
    private final CommentsService commentsService;

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable(name="code") String code) {
        return ResponseEntity.ok(commentsService.getAllByCode(code));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<Comment> addReaction(@CurrentUser AppUser user,
                                                     @PathVariable(name="code") String code,
                                                     @PathVariable(name="commentId") String commentId,
                                                     @RequestParam String reaction) {
        return ResponseEntity.ok(commentsService.updateReaction(user, code, commentId, reaction));
    }

    @PostMapping("/comments/{commentId}/sub_comments")
    public ResponseEntity<Comment> addSubComment(@CurrentUser AppUser user,
                                              @PathVariable(name="code") String code,
                                              @PathVariable(name="commentId") String commentId,
                                              @RequestBody Comment comment) {
        comment.setId(UUID.randomUUID().toString());
        comment.setAuthorId(user.getId());
        comment.setAuthorName(user.getNickname());
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setReactions(new ArrayList<>());
        Comment result = commentsService.addSubComment(code, commentId, comment);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@CurrentUser AppUser user,
                                              @PathVariable(name="code") String code,
                                              @RequestBody Comment comment) {
        comment.setId(UUID.randomUUID().toString());
        comment.setAuthorId(user.getId());
        comment.setAuthorName(user.getNickname());
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setReactions(new ArrayList<>());
        Comment result = commentsService.addComment(code, comment);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@CurrentUser AppUser user,
                                                 @PathVariable(name="code") String code,
                                                 @PathVariable(name="commentId") String commentId,
                                                 @RequestParam String newText) {
        Comment result = commentsService.updateComment(user, code, commentId, newText);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Comment> deleteComment(@CurrentUser AppUser user,
                                                 @PathVariable(name="code") String code,
                                                 @PathVariable(name="commentId") String commentId) {
        Comment result = commentsService.deleteComment(user, code, commentId);
        return ResponseEntity.ok(result);
    }
}

package io.github.reconsolidated.weaskedapi.comments;

import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUser;
import io.github.reconsolidated.weaskedapi.authentication.currentUser.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@CurrentUser AppUser user, @RequestBody Comment comment) {
        comment.setAuthorId(user.getId());
        comment.setAuthorName(user.getUserName());
        Comment result = commentsService.addComment(comment);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/clear_all")
    public ResponseEntity<?> clearAll() {
        commentsService.deleteAll();
        return ResponseEntity.ok().build();
    }
}

package io.github.reconsolidated.weaskedapi.comments;

import io.github.reconsolidated.weaskedapi.reactions.Reaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    private String id;
    private Long authorId;
    private String authorName;
    @Column(length = 10000)
    private String text;
    private Long createdAt;
    private List<Reaction> reactions = new ArrayList<>();
}

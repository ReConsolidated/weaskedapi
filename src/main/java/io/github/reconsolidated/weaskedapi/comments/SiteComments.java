package io.github.reconsolidated.weaskedapi.comments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Getter
@Document("site_comments")
@NoArgsConstructor
public class SiteComments {
    @Id
    private String id;
    private List<Comment> comments;

    public SiteComments(String code) {
        this.id = code;
        this.comments = new ArrayList<>();
    }
}

package io.github.reconsolidated.weaskedapi.comments;

import io.github.reconsolidated.weaskedapi.reactions.Reaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(generator = "comment_id_seq")
    private Long id;
    private Long authorId;
    private String code;
    private String authorName;
    @Column(length = 10000)
    private String text;
    private Date createdAt = Date.valueOf(LocalDate.now());
    @OneToMany(fetch = FetchType.EAGER)
    private List<Reaction> reactions = new ArrayList<>();
}

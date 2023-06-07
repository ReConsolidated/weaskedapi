package io.github.reconsolidated.weaskedapi.reactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Reaction {
    @Id
    @GeneratedValue(generator = "reaction_id_seq")
    private Long id;
    private Long authorId;
    private Long commentId;
    private String reactionType;
    private Date createdAt;
}

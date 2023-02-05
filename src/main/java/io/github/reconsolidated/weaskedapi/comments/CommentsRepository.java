package io.github.reconsolidated.weaskedapi.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCode(String code);
}

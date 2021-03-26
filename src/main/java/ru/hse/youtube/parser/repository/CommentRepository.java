package ru.hse.youtube.parser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.youtube.parser.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query(nativeQuery=true, value="SELECT *  FROM question ORDER BY random() LIMIT 10")
    Comment findRandomComment(int rowNumber);

}

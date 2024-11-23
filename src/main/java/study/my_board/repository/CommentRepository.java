package study.my_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.my_board.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


}

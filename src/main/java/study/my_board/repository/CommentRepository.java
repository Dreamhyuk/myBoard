package study.my_board.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.my_board.domain.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


//    @Query("select c from Comment c join fetch c.post p")
    Optional<Comment> findByPostIdAndId(Long postId, Long commentId);

    //페이징 처리된 댓글 조회
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    //전체 댓글 조회
    List<Comment> findByPostId(Long postId);

    //댓글과 작성자를 함께 조회
    @Query("select c from Comment c join fetch c.member where c.post.id = :postId")
    Page<Comment> findCommentWithMemberByPostId(@Param("postId") Long postId, Pageable pageable);

    long countByPostId(Long id);
}

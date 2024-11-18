package study.my_board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.my_board.domain.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    //select p from Post p where p.title = :title
    List<Post> findByTitle(String title);

    List<Post> findByTitleOrContent(String title, String content);

    Page<Post> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

//    List<>

}
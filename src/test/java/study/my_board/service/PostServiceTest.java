package study.my_board.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import study.my_board.domain.Post;
import study.my_board.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @PersistenceContext EntityManager em;
    @Autowired PostService postService;
    @Autowired PostRepository postRepository;


    @Test
    public void writeAll() {
        //given
        List<Post> postList = IntStream.range(0, 120)
                .mapToObj(i -> Post.builder()
                        .title("게시글" + i)
                        .content("내용" + i)
                        .build())
                .collect(Collectors.toList());

        //when
        postService.writeAll(postList);

        //then
        assertEquals("저장한 주문 수 확인", 120, postRepository.findAll().size());
    }
}
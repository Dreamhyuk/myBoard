package study.my_board.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import study.my_board.domain.Post;
import study.my_board.repository.PostRepository;
import study.my_board.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;
    private final PostRepository postRepository;


    @GetMapping("/board")
    List<Post> all(@RequestParam(required = false, defaultValue = "") String title,
                   @RequestParam(required = false, defaultValue = "") String content) {
        if (StringUtils.hasText(title) && (StringUtils.hasText(content))) {
            return postRepository.findByTitleOrContent(title, content);
        } else {
            return postRepository.findAll();
        }
    }

    @PostMapping("/board")
    Post newPost(@RequestBody Post newPost) {
        return postRepository.save(newPost);
    }


    @GetMapping("/board/{id}")
    Post one(@PathVariable Long id) {

        return postRepository.findById(id).orElse(null);
    }

    @PutMapping("/board/{id}")
    Post replacePost(@RequestBody Post newPost, @PathVariable Long id) {

        return postRepository.findById(id)
                .map(Post -> {
                    Post.setTitle(newPost.getTitle());
                    Post.setContent(newPost.getContent());
                    return postRepository.save(Post);
                })
                .orElseGet(() -> {
                    return postRepository.save(newPost);
                });
    }

//    @Secured("ROLE_ADMIN")
    @PreAuthorize("@postService.canDeletePost(#id, principal.id)") //메서드 호출 전에 권한을 검사.
    @DeleteMapping("/board/{id}")
    void deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
    }


}

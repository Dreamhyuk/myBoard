package study.my_board.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.my_board.domain.Comment;
import study.my_board.dto.CommentDto;
import study.my_board.service.CommentService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentApiController {

    private final CommentService commentService;


    //댓글 생성
    @PostMapping("/board/{id}/comments")
    public ResponseEntity<Comment> write(@PathVariable Long id,
                                         @RequestBody CommentDto.Request comment, Principal principal) {

        return null;
    }
}

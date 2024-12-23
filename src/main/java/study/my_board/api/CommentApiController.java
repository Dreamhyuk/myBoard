package study.my_board.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.my_board.authentication.CustomUserDetails;
import study.my_board.domain.Comment;
import study.my_board.dto.CommentDto;
import study.my_board.repository.CommentRepository;
import study.my_board.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentApiController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    /* 댓글 작성 */
    @PostMapping("/board/view/{postId}/comments")
    public ResponseEntity<CommentDto.Response> write(@PathVariable Long postId,
                                                     @RequestBody @Valid CommentDto.Request request,
                                                     @AuthenticationPrincipal CustomUserDetails currentUser) {

        commentService.save(currentUser.getId(), postId, request);
        CommentDto.Response response = new CommentDto.Response(request.toEntity());

        return ResponseEntity.ok(response);
    }

    /* 페이징 조회 */
    @GetMapping("/board/view/{postId}/pagedComments")
    public List<CommentDto.Response> getPagedComment(@PathVariable Long postId) {
        return commentService.findPagedComment(postId, 5);
    }

    /* 전체 조회 */
    @GetMapping("/board/view/{postId}/comments")
    public List<CommentDto.Response> getAllComment(@PathVariable Long postId) {
        return commentService.findAll(postId);
    }

    /* 댓글 수정 */
    @PutMapping("/board/view/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto.Response> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                             @RequestBody CommentDto.Request request) {
        commentService.update(postId, commentId, request);
        CommentDto.Response response = new CommentDto.Response(request.toEntity());

        return ResponseEntity.ok(response);
    }

    /* 삭제 */
    @DeleteMapping("/board/view/{postId}/comments/{commentId}")
    public ResponseEntity<Long> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.delete(postId, commentId);

        return ResponseEntity.ok(commentId);
    }


}

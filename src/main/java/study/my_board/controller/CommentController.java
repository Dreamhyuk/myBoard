package study.my_board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import study.my_board.authentication.CustomUserDetails;
import study.my_board.domain.Member;
import study.my_board.dto.CommentDto;
import study.my_board.repository.MemberRepository;
import study.my_board.service.CommentService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final SimpMessagingTemplate messagingTemplate;
    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @MessageMapping("/comments/{postId}") // 클라이언트가 "/app/comments"로 메시지 전송
    @SendTo("/topic/comments/{postId}")  // 해당 게시글을 구독 중인 클라이언트로 메시지 전송
    public Page<CommentDto.Response> broadcastComment(@DestinationVariable Long postId, @Payload CommentDto.Request request,
                                                      Principal principal) {
//        Long postId = request.getPost().getId();
        if (request.getComment() == null && postId == null) {
            throw new IllegalArgumentException("댓글 내용 or postId가 없습니다.");
        }

        // 현재 로그인한 사용자 가져오기
        String username = principal.getName();
        Member member = memberRepository.findByUsername(username);

        commentService.save(member.getId(), postId, request);

        //최신 댓글 리스트 조회
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        return commentService.findPagedComment(postId, pageable);
    }
}

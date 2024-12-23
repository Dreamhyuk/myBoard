package study.my_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.my_board.domain.Comment;
import study.my_board.domain.Member;
import study.my_board.domain.Post;
import study.my_board.dto.CommentDto;
import study.my_board.repository.CommentRepository;
import study.my_board.repository.MemberRepository;
import study.my_board.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    //댓글 생성
    @Transactional
    public Long save(Long memberId, Long postId, CommentDto.Request request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found!"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found!"));

        request.setMember(member);
        request.setPost(post);

        Comment comment = request.toEntity();
        commentRepository.save(comment);

        return comment.getId();
    }


    /* 페이징 댓글 조회 */
    public List<CommentDto.Response> findPagedComment(Long postId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        List<CommentDto.Response> result = commentRepository.findByPostId(postId, pageable)
                .getContent().stream()
                .map(c -> new CommentDto.Response(c))
                .collect(Collectors.toList());
        return result;
    }

    /* 댓글 조회 */
    public List<CommentDto.Response> findAll(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found!"));

        List<Comment> comments = post.getComments();
        return comments.stream()
                .map(c -> new CommentDto.Response(c))
                .collect(Collectors.toList());
    }

    public Page<CommentDto.Response> findAll(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        Page<CommentDto.Response> result = comments.map(c -> new CommentDto.Response(c));
        return result;
    }

    /* 댓글 수정 */
    @Transactional
    public void update(Long postId, Long commentId, CommentDto.Request request) {
        Comment comment = commentRepository.findByPostIdAndId(postId, commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found!!"));

        comment.updateComment(request.getComment());
    }

    /* 삭제 */
    @Transactional
    public void delete(Long postId, Long commentId) {
        Comment comment = commentRepository.findByPostIdAndId(postId, commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found!!"));

        commentRepository.delete(comment);
    }

}

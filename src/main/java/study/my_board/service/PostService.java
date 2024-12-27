package study.my_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.my_board.domain.Member;
import study.my_board.domain.Post;
import study.my_board.dto.MemberDto;
import study.my_board.dto.PostDto;
import study.my_board.repository.MemberRepository;
import study.my_board.repository.PostRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    //게시글 리스트 처리
//    @Transactional(readOnly = true)
//    public Page<PostDto> boardList(Pageable pageable) {
//        Page<Post> postPage = postRepository.findAll(pageable);
//        return postPage.map(this::toPostDto);
//    }
    @Transactional(readOnly = true)
    public Page<PostDto.Response> findBySearchKeyword(String title, String content, Pageable pageable) {
        Page<Post> postList = postRepository.findByTitleContainingOrContentContaining(title, content, pageable);

        Page<PostDto.Response> result = postList.map(p -> new PostDto.Response(p));
        return result;
    }


    //글 작성
    @Transactional
    public Long write(PostDto.Request request, Long memberId) {
        //엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        request.setMember(member);
        Post post = request.toEntity();
        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public void writeAll(List<Post> postList) {
        postRepository.saveAll(postList);
    }

    //특정 게시글 불러오기, 엔티티로 반환
    @Transactional
    public PostDto.Response findPost(Long postId) {

        //엔티티 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found!"));

        //Dto 로 변환 후 반환
        return new PostDto.Response(post);
    }


    //수정 권한: 작성자
    public boolean canEditPost(Long postId, Long currentUserId) {
        return isAuthor(postId, currentUserId);
    }

    //삭제 권한: 작성자 & admin
    public boolean canDeletePost(Long postId, Long currentUserId) {
        return isAuthor(postId, currentUserId) || memberService.isAdmin(currentUserId);
    }

    public boolean isAuthor(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()  -> new IllegalArgumentException("Post not found!"));
        boolean result = post.getMember().getId().equals(currentUserId);

        return result;
    }

    @Transactional
    public Post updatePost(Long postId, PostDto.Request request) {
        Post post = postRepository.findById(postId).orElse(null);
        post.updatePost(request.getTitle(), request.getContent());

        return postRepository.save(post);
    }

//    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @postService.isAuthor(#postId, authentication.principal.id)")
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

}

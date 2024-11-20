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
    public Page<PostDto> findBySearchKeyword(String title, String content, Pageable pageable) {
        Page<Post> postPage = postRepository.findByTitleContainingOrContentContaining(title, content, pageable);

        return postPage.map(this::toPostDto);
    }

    private PostDto toPostDto(Post post) {
        PostDto postDto = PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .memberDto(new MemberDto(post.getMember()))
                .build();

        return postDto;
    }

    //글 작성
    @Transactional
    public Long write(PostDto postDto, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));;

        //Dto를 엔티티로 변환
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .member(member)
                .build();

        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public void writeAll(List<Post> postList) {
        postRepository.saveAll(postList);
    }

    //특정 게시글 불러오기, 엔티티로 반환
    @Transactional
    public PostDto findPost(Long postId) {

        //엔티티 조회
        Post post = postRepository.findById(postId).orElse(null);
        Member member = memberRepository.findById(post.getMember().getId()).orElse(null);

        MemberDto memberDto = MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .enabled(member.getEnabled())
                .build();

        //Dto로 변환 후 반환
        return new PostDto(post, memberDto);
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
    public Post updatePost(Long postId, PostDto updateDto) {

        Post post = postRepository.findById(postId).orElse(null);
        post.updatePost(updateDto.getTitle(), updateDto.getContent());

        return postRepository.save(post);
    }

//    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @postService.isAuthor(#postId, authentication.principal.id)")
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

}

package study.my_board.api;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import study.my_board.domain.Member;
import study.my_board.domain.Post;
import study.my_board.repository.MemberRepository;
import study.my_board.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;


    //memberRole 에서 Role이 Lazy여서 프록시로 관리되기 때문에 직렬화가 불가능함. (엔티티 직접 반환)
    @GetMapping("/users")
    List<Member> all() {
        return memberRepository.findAll();
//        memberService.
    }


    @PostMapping("/users")
    Member newMember(@RequestBody Member newMember) {
        return memberRepository.save(newMember);
    }

    @GetMapping("/users/{id}")
    Member one(@PathVariable Long id) {

        return memberRepository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    Member replaceMember(@RequestBody Member newMember, @PathVariable Long id) {

        return memberRepository.findById(id)
                .map(member -> {
//                    member.setTitle(newMember.getTitle());
//                    member.setContent(newMember.getContent());
//                    member.setPosts(newMember.getPosts());
                    member.getPosts().clear();
                    member.getPosts().addAll(newMember.getPosts());
                    for (Post post : member.getPosts()) {
                        post.setMember(member);
                        break;
                    }
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    return memberRepository.save(newMember);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
    }


}

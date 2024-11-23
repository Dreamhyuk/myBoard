package study.my_board.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.my_board.domain.Member;
import study.my_board.domain.MemberRole;
import study.my_board.domain.Role;
import study.my_board.dto.MemberDto;
import study.my_board.repository.MemberRepository;
import study.my_board.repository.MemberRoleRepository;
import study.my_board.repository.RoleRepository;
//import study.my_board.domain.Member;
//import study.my_board.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(@Valid MemberDto memberDto) {

        //password를 암호화
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());

        Member member = Member.createMember(memberDto.getUsername(), encodedPassword);

        Role defaultRole = roleRepository.findRoleByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Default role not found"));
        member.addRole(defaultRole);

        memberRepository.save(member);

        return member.getId();
    }

    //role 타입 비교
    public boolean isAdmin(Long memberId) {
//        List<String> roles = memberRepository.findRolesByMemberId(memberId);
//        return roles.contains("ROLE_ADMIN");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found!"));

        return member.getMemberRoles().stream()
                .map(MemberRole::getRole)
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

//    public Long findIdByUsername(String username) {
//        Member member = memberRepository.findOneByUsername(username);
//
//        return member.getId();
//    }


}

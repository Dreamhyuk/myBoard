package study.my_board.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

//    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberRole> memberRoles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    private String username;
    private String password;
    private Boolean enabled;

    //== 연관관계 메서드 ==//
    public void addRole(Role role) {
        MemberRole memberRole = MemberRole.createMemberRole(this, role);
        memberRoles.add(memberRole);
    }

    public void addMemberRole(MemberRole memberRole) {
        memberRoles.add(memberRole);
        memberRole.setMember(this);
    }

    @Builder
    public Member(Long id, String username, String password, Boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    //== 생성 메서드 ==//
    public static Member createMember(String username, String password, MemberRole... memberRoles) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);
        for (MemberRole memberRole: memberRoles) {
            member.addMemberRole(memberRole);
        }
        member.setEnabled(true);

        return member;
    }

}

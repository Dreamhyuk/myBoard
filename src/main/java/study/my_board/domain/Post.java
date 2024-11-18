package study.my_board.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "posts")
@Getter @Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @OneToMany(mappedBy = "post")
//    @Builder.Default
//    private List<Comment> comments = new ArrayList<>();

    @NotNull
    @Size(min = 2, max = 60)
    private String title;

    @NotNull
    @Size(min = 2)
    private String content;


    //== 생성 메서드 ==//
    @Builder
    public static Post createPost(Long id, Member member, String title, String content) {
        return new Post(id, member, title, content);
    }


    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }


    //== 게시글 수정 메서드 ==//
    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

}

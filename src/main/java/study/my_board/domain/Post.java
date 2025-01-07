package study.my_board.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "posts")
@Getter @Setter
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


    @NotNull
    @Size(min = 2, max = 60)
    private String title;

    @NotNull
    @Size(min = 2)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private final List<Comment> comments = new ArrayList<>();

    private int views;

    @CreatedDate
    private String createdDate;

    @LastModifiedDate
    private String modifiedDate;


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

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public Post(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    @Builder
    public Post(Member member, String title, String content, int views, String createdDate, String modifiedDate) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.views = views;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

}

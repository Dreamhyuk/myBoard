package study.my_board.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "comments")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Size(min = 2, message = "댓글을 2글자 이상 작성해주세요.")
    private String comment; //댓글 내용


    //== 생성 메서드 ==//
    public static Comment createComment(Member member, Post post, String comments) {
        Comment comment = new Comment();
        comment.member = member;
        comment.post = post;
        comment.comment = comments;

        return comment;
    }

    //== 댓글 수정 메서드 ==//
    public void updateComment(String comment) {
        this.comment = comment;
    }

}

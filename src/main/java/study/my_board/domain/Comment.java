package study.my_board.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
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


    private String comment; //댓글 내용

//    private LocalDateTime createDate;
//
//    private LocalDateTime modifiedDate;



    //== 댓글 수정 메서드 ==//
    public void updateComment(String comment) {
        this.comment = comment;
    }

}

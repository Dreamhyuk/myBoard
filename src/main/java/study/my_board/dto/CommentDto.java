package study.my_board.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import study.my_board.domain.Comment;
import study.my_board.domain.Member;
import study.my_board.domain.Post;


public class CommentDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private Long id;
        private Member member;
        private Post post;
        @NotNull(message = "댓글 내용은 비어 었을 수 없습니다!!!!!!!")
        @Size(min = 2)
        private String comment;

        public Request(String comment) {
        }

        /* Dto -> Entity */
        public Comment toEntity() {
            Comment comments = Comment.builder()
                    .id(id)
                    .member(member)
                    .post(post)
                    .comment(comment)
                    .build();

            return comments;
        }
    }


    /**
     * return할 클래스
     * Entity 클래스를 생성자 파라미터로 받아 Dto로 변환 후 return.
     */
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long memberId;
        private Long postId;
        private String comment;
        private String username;

        /* Entity -> Dto */
        public Response(Comment comment) {
            this.id = comment.getId();
            this.memberId = comment.getMember().getId();
            this.postId = comment.getPost().getId();
            this.comment = comment.getComment();
            this.username = comment.getMember().getUsername();
        }

    }
}

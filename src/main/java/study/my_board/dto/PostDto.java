package study.my_board.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.my_board.domain.Member;
import study.my_board.domain.Post;

import java.util.List;
import java.util.stream.Collectors;


public class PostDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        private Long id;
        @NotNull
        @Size(min = 2, max = 60, message = "제목은 2자 이상 60자 이하입니다.")
        private String title;

        @NotNull
        @Size(min = 2, message = "내용을 2자 이상 입력하세요.")
        private String content;
        private Member member;

        /* Dto -> Entity */
        public Post toEntity() {
            Post post = Post.builder()
                    .id(id)
                    .title(title)
                    .content(content)
                    .member(member)
                    .build();
            return post;
        }
    }

    @Data
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String author;
        private Long memberId;
        private List<CommentDto.Response> comments;

        /* Entity -> Dto */
        public Response(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.author = post.getMember().getUsername();
            this.memberId = post.getMember().getId();
//            this.comments = post.getComments().stream()
//                    .map(c -> new CommentDto.Response(c))
//                    .collect(Collectors.toList());
        }
    }

}

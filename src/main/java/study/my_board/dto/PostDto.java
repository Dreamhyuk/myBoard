package study.my_board.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.my_board.domain.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 60, message = "제목은 2자 이상 60자 이하입니다.")
    private String title;

    @NotNull
    @Size(min = 2, message = "내용을 입력하세요.")
    private String content;

    private MemberDto memberDto;

    public PostDto(Post post, MemberDto memberDto) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.memberDto = memberDto;
    }
}

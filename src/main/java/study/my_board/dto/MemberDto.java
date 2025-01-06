package study.my_board.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import study.my_board.domain.Member;

public class MemberDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long id;
        private String username;
        private String password;
        private Boolean enabled;

        /* Dto -> Entity */
        public Member toEntity() {
            Member member = Member.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .enabled(enabled)
                    .build();

            return member;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String username;
        private Boolean enabled;

        /* Entity -> Dto */
        public Response(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.enabled = member.getEnabled();
        }
    }

//    public MemberDto(Member member) {
//        this.id = member.getId();
//        this.username = member.getUsername();
//        this.password = member.getPassword();
//        this.enabled = member.getEnabled();
//    }
}

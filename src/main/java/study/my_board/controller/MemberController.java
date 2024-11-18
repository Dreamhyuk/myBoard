package study.my_board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.my_board.dto.MemberDto;
import study.my_board.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "account/signup";
    }

    @PostMapping("/signup")
    public String signup(MemberDto memberDto) {
        memberService.join(memberDto);
        return "redirect:/";
    }
}

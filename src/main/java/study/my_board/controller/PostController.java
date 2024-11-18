package study.my_board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import study.my_board.authentication.CustomUserDetails;
import study.my_board.dto.PostDto;
import study.my_board.service.PostService;
import study.my_board.validator.PostValidator;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostValidator boardValidator;


    @GetMapping()
    public String index() {
        return "home";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(required = false, defaultValue = "") String searchKeyword) {

        Page<PostDto> postDtos = postService.findBySearchKeyword(searchKeyword, searchKeyword, pageable);

        int nowPage = postDtos.getPageable().getPageNumber() + 1; //page가 0부터 시작하기 때문에 +1
        int startPage = Math.max(1, nowPage - 3);
        int endPage = Math.min(postDtos.getTotalPages(), nowPage + 3);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("list", postDtos);

        return "boardList";
    }

    @GetMapping("/board/write")
    public String writePost(Model model) {
        model.addAttribute("board", new PostDto());

        return "writeForm";
    }

    @PostMapping("/board/write")
    public String writePro(@Valid @ModelAttribute("board") PostDto postDto, BindingResult bindingResult,
                           Authentication authentication) throws Exception {
        boardValidator.validate(postDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "writeForm";
        }

        String username = authentication.getName();
        postService.write(postDto, username);
        return "redirect:/board/list";
    }

    @GetMapping("/board/view")
    public String viewPost(Long postId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {

        PostDto postDto = postService.findPost(postId);
        Long currentUserId = currentUser.getId();

        model.addAttribute("board", postDto);
        model.addAttribute("canEdit", postService.canEditPost(postId, currentUserId));
        model.addAttribute("canDelete", postService.canDeletePost(postId, currentUserId));

        return "view";
    }

    @GetMapping("/board/modify/{postId}")
    public String modifyPost(@PathVariable Long postId, Model model) {

        PostDto findPost = postService.findPost(postId);
        model.addAttribute("post", findPost);

        return "modifyPost";
    }

    @PostMapping("/board/update/{postId}")
    public String updatePost(@PathVariable Long postId,
                             @Valid @ModelAttribute("post") PostDto postDto,BindingResult bindingResult, Model model) throws Exception {
        boardValidator.validate(postDto, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("id", postDto.getId());
            return "modifyPost";
        }

        postService.updatePost(postId, postDto);

        return "redirect:/board/list";
    }

    @GetMapping("/board/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);

        return "redirect:/board/list";
    }


//    @GetMapping("/board/join")
//    public String login() {
//        return "login";
//    }


}

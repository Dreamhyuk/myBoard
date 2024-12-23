package study.my_board.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import study.my_board.dto.PostDto;

@Component
public class PostValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PostDto.Request.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        PostDto.Request post = (PostDto.Request) obj;
        if (!StringUtils.hasText(post.getContent())) {
           errors.rejectValue("content", "key", "");
        }
    }

}

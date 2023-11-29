package app.codingGround.api.community.dto;

import app.codingGround.api.entity.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommunityRegisterDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String postTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String postContent;

    private User user;
}

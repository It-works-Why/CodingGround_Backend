package app.codingGround.api.admin.dto;

import app.codingGround.api.account.entitiy.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class NoticeRegisterDto {

    // @NotNull : null만 허용x, "", " "은 허용
    // @NotBlank : null, "", " " 모두 허용 x
    @NotBlank(message = "제목을 입력해주세요.")
    private String noticeTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String noticeContent;

    private User user;
}

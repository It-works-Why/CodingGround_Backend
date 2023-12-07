package app.codingGround.api.account.dto.request;


import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@ToString
public class UserInfoEditDto {


    @NotBlank(message = "닉네임은 20자 이하, 5자이상 으로 작성 해주세요.")
    @NotNull(message = "닉네임은 20자 이하, 5자이상 으로 작성 해주세요.")
    @Length(max = 20,min = 5, message = "닉네임은 20자 이하, 5자이상 으로 작성 해주세요.")
    private String userNickname;

    private String userEmail;

    @NotNull(message = "소속을 선택해 주십시오.")
    private String userAffiliation;

    @NotNull(message = "소속 상세는 입력해 주십시오.")
    @NotBlank(message = "소속 상세는 입력해 주십시오.")
    private String userAffiliationDetail;

}

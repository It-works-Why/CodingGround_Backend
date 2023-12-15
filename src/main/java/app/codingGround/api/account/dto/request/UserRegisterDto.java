package app.codingGround.api.account.dto.request;


import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
@Getter
@ToString
public class UserRegisterDto {

    @NotBlank(message = "아이디는 최소 5자 이상 20자 이하로 작성 해주세요.")
    @Pattern(
            regexp = "^[A-Za-z\\d]+$",
            message = "아이디는 최소 5자 이상 20자 이하로 작성 해주세요."
    )
    @Length(max = 20, min = 5, message = "아이디는 최소 5자 이상 20자 이하로 작성 해주세요.")
    @NotNull(message = "아이디는 최소 5자 이상 20자 이하로 작성 해주세요.")
    private String userId;

    @NotBlank(message = "패스워드는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    @Length(min = 8, message = "패스워드는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "패스워드는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    @Pattern(
            regexp = "^[A-Za-z\\d@$!%*?&]{8,}$",
            message = "패스워드는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    @NotNull
    private String userPassword;

    @NotBlank(message = "닉네임은 8자 이하, 5자이상 으로 작성 해주세요.")
    @NotNull(message = "닉네임은 8자 이하, 5자이상 으로 작성 해주세요.")
    @Length(max = 8,min = 5, message = "닉네임은 8자 이하, 5자이상 으로 작성 해주세요.")
    private String userNickname;

    @Email(message = "이메일을 다시 입력해 주십시오.")
    @NotBlank(message = "이메일을 다시 입력해 주십시오.")
    @NotNull(message = "이메일을 다시 입력해 주십시오.")
    private String userEmail;

    @NotNull(message = "소속을 선택해 주십시오.")
    private String userAffiliation;

    @NotNull(message = "소속 상세는 입력해 주십시오.")
    @NotBlank(message = "소속 상세는 입력해 주십시오.")
    private String userAffiliationDetail;
    private String userProfileImg;
}

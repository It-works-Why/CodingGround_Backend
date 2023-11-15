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

    @NotBlank(message = "아이디는 공백은 허용 되지 않습니다.")
    @Length(max = 20,min = 5, message = "아이디는 최소 5자 이상 20자 이하로 작성 해주세요.")
    @NotNull(message = "아이디는 Null 값은 허용 되지 않습니다.")
    private String userId;

    @NotBlank(message = "패스워드는 공백은 허용 되지 않습니다.")
    @Length(min = 8, message = "패스워드는 최소 8자 이상 작성해 주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "패스워드는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    @NotNull
    private String userPassword;

    @NotBlank(message = "닉네임은 공백은 허용 되지 않습니다.")
    @NotNull(message = "닉네임은 Null 값은 허용 되지 않습니다.")
    @Length(max = 20,min = 5, message = "닉네임은 20자 이하, 5자이상 으로 작성 해주세요.")
    private String userNickname;

    @Email(message = "이메일 형태가 아닙니다.")
    @NotBlank(message = "이메일은 공백은 허용 되지 않습니다.")
    @NotNull(message = "이메일은 Null 값은 허용 되지 않습니다.")
    private String userEmail;

    @NotNull(message = "소속은 Null 값은 허용 되지 않습니다.")
    private String userAffiliation;

    @NotNull(message = "소속 상세는 Null 값은 허용 되지 않습니다.")
    @NotBlank(message = "소속 상세는 공백은 허용 되지 않습니다.")
    private String userAffiliationDetail;
    private String userProfileImg;
}

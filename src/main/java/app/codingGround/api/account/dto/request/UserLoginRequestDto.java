package app.codingGround.api.account.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserLoginRequestDto {
    @NotBlank(message = "로그인 정보가 틀립니다.")
    @Length(max = 20,min = 5, message = "로그인 정보가 틀립니다.")
    @NotNull(message = "로그인 정보가 틀립니다.")
    private String userId;

    @NotBlank(message = "로그인 정보가 틀립니다.")
    @Length(min = 8, message = "로그인 정보가 틀립니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "로그인 정보가 틀립니다."
    )
    @NotNull(message = "로그인 정보가 틀립니다.")
    private String userPassword;
}
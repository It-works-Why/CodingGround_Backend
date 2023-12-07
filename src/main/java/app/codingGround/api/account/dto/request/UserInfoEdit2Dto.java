package app.codingGround.api.account.dto.request;


import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class UserInfoEdit2Dto {

    private String userId;
    private String userPassword;
    private String userNickname;
    private String userEmail;

    @NotNull(message = "소속을 선택해 주십시오.")
    private String userAffiliation;

    @NotNull(message = "소속 상세는 입력해 주십시오.")
    @NotBlank(message = "소속 상세는 입력해 주십시오.")
    private String userAffiliationDetail;
    private String userProfileImg;
}

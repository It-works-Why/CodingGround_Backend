package app.codingGround.api.account.dto.response;

import app.codingGround.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserInfoDto {

    private String userId;
    private String userNickname;
    private String userEmail;
    private String userAffiliation;
    private String userAffiliationDetail;

    public EditUserInfoDto (User user) {
        this.userId = user.getUserId();
        this.userNickname = user.getUserNickname();
        this.userEmail = user.getUserEmail();
        this.userAffiliation = user.getUserAffiliation();
        this.userAffiliationDetail = user.getUserAffiliationDetail();
    }
}

package app.codingGround.api.admin.dto.response;

import lombok.Getter;
import org.apache.ibatis.type.Alias;

@Getter
@Alias("UserManageListDto")
public class UserManageListDto {

    private Long userNum;
    private String userProfileImg;
    private String userNickname;
    private String userEmail;
    private String userId;
    private String userAffiliation;
    private String userAffiliationDetail;
    private String userStatus;
}

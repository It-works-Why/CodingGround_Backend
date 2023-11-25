package app.codingGround.api.account.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class UserInfoFromToken {
    private String userId;
    private String userRole;
    private String userNickname;


    @Builder
    public UserInfoFromToken(String userId, String userRole, String userNickname) {
        this.userId = userId;
        this.userRole = userRole;
        this.userNickname = userNickname;
    }
}

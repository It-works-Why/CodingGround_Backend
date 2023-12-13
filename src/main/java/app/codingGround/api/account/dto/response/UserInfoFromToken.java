package app.codingGround.api.account.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class UserInfoFromToken {
    private String userId;
    private String userRole;
    private String userNickname;
    private long rankNum;


    @Builder
    public UserInfoFromToken(String userId, String userRole, String userNickname, long rankNum) {
        this.userId = userId;
        this.userRole = userRole;
        this.userNickname = userNickname;
        this.rankNum = rankNum;
    }
}

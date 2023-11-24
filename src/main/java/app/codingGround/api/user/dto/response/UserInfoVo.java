package app.codingGround.api.user.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class UserInfoVo {
    private String userNickname;
    private String userAffiliationDetail;
    private String userProfileImg;
    private String currentRank;
    private List<Ranking> ranking;
    private String gameBadge;
    private String gameLanguage;




}

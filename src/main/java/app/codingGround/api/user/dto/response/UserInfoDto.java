package app.codingGround.api.user.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@Builder
@Alias("UserInfoDto")
public class UserInfoDto {
    private String userNickname;
    private String userAffiliationDetail;
    private String userProfileImg;
    private String rankNum;
    private String matches;
    private String wins;
    private String losses;
    private String recordPercentage;
/*    private List<RankingDto> ranking;*/
  /*  private List<GameBadgeDto> gameBadge;
    private List<GameLanguageDto> gameLanguage;*/
}

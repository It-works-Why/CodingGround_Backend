package app.codingGround.api.user.dto.response;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Builder
@Alias("InfoDto")
public class InfoDto {
    private String userNickname;
    private String userAffiliationDetail;
    private String userProfileImg;
    private String rankNum;
    private String matches;
    private String wins;
    private String losses;
    private String recordPercentage;
}

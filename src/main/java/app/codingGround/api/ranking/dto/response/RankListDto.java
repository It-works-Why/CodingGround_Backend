package app.codingGround.api.ranking.dto.response;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@Data
@Builder
@Alias("RankListDto")
public class RankListDto {

    private long rankOrder;
    private String userNickname;
    private String userProfileImg;
    private String rankNum;
    private String rankScore;
    private double recordPercentage;
    private String seasonNum;
}

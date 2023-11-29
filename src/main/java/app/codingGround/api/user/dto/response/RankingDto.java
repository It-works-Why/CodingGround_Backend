package app.codingGround.api.user.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Data
@Builder
@Alias("RankingDto")
public class RankingDto {
    private Integer count1;
    private Integer count2;
    private Integer count3;
    private Integer count4;
    private Integer count5;

}

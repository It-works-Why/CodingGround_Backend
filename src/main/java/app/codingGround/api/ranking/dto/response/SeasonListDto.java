package app.codingGround.api.ranking.dto.response;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@Data
@Builder
@Alias("SeasonListDto")
public class SeasonListDto {

    private String seasonName;

}

package app.codingGround.api.ranking.dto.response;

import app.codingGround.api.user.dto.response.PageNumDto;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Data
@NoArgsConstructor
@Alias("RankingPageDto")
public class RankingPageDto {

    private List<RankListDto> rankListDto;
    private List<SeasonListDto> seasonListDto;
    private PageNumDto totalPageNum;
    private int pageNum;


}

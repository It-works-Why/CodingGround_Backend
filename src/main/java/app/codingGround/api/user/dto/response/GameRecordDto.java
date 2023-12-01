package app.codingGround.api.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@NoArgsConstructor
@Alias("GameRecordDto")
public class GameRecordDto {

    private List<GameInfoDto> GameInfoData;

}

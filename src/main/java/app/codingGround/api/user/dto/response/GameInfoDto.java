package app.codingGround.api.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@NoArgsConstructor
@Alias("GameInfoDto")
public class GameInfoDto {
    private String gameNum;
    private String gameType;
    private String languageName;
    private String timeDifference;
    private String userNicknames;
    private String userProfileImgs;
    private List<String> userNicknamesList;
    private List<String> userProfileImgList;

}

package app.codingGround.api.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@NoArgsConstructor
@Alias("GameRecordRoundOneDto")
public class GameRecordRoundOneDto {

    private int gameNum;
    private int round;
    private String questionTitle;
    private int questionLimitTime;
    private int questionDifficult;
    private String questionContent;
    private String roundRecordAnswer;
    private int roundRecordAnswerCorrect;



}

package app.codingGround.api.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;


@Data
@NoArgsConstructor
@Alias("GameRecordRoundTwoDto")
public class GameRecordRoundTwoDto {

    private int gameNum;
    private int round;
    private String questionTitle;
    private int questionLimitTime;
    private int questionDifficult;
    private String questionContent;
    private String roundRecordAnswer;
    private int roundRecordAnswerCorrect;



}

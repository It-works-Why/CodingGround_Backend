package app.codingGround.api.battle.dto.response;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;
import java.util.List;

@Data
@Alias("QuestionDto")
@ToString
public class QuestionDto {
    private String baseCode;

    private String questionTitle;

    private String questionContent;

    private int questionDifficult;

    private int questionLimitTime;

    private Timestamp endTime;

    private int round;

    private Integer languageCode;

    private String languageName;

}

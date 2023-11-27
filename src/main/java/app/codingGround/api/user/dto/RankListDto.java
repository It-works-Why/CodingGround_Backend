package app.codingGround.api.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RankListDto {

    private long rank;
    private String userNickname;
    private String rankNum;
    private String rankScore;
    private int visitProbability;
}

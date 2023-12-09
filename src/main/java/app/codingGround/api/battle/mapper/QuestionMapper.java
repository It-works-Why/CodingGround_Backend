package app.codingGround.api.battle.mapper;

import app.codingGround.api.battle.dto.response.GameDto;
import app.codingGround.api.battle.dto.response.QuestionDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {
    QuestionDto getQuestion(GameDto game);
}

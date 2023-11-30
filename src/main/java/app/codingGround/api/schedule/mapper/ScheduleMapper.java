package app.codingGround.api.schedule.mapper;

import app.codingGround.api.schedule.dto.InsertSeasonDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduleMapper {

    InsertSeasonDto insertSeason();

}

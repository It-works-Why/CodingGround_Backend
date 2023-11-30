package app.codingGround.api.schedule.service;

import app.codingGround.api.schedule.dto.InsertSeasonDto;
import app.codingGround.api.schedule.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;

    public InsertSeasonDto insertSeason() {
        return scheduleMapper.insertSeason();
    }
}

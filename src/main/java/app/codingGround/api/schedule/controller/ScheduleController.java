package app.codingGround.api.schedule.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ScheduleController {

    @Transactional
    @Scheduled(cron = "0 0 0 1 1,4,7,10 *", zone = "Asia/Seoul")
    public void insertSeasonScheduler() {




    }
}

package app.codingGround.api.schedule.controller;

import app.codingGround.api.schedule.dto.InsertSeasonDto;
import app.codingGround.api.schedule.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerService schedulerService;

    @Transactional
    @Scheduled(cron = "0 0 0 1 1,4,7,10 *")
//    @Scheduled(cron = "30 25 17 1 12 *")
    public void insertSeasonScheduler() {

        InsertSeasonDto insertSeasonDto = new InsertSeasonDto();
        LocalDateTime currentTime = LocalDateTime.now(clock);

        log.info("ctrl + f 로 검색!");
        log.info("스케쥴러 동작 중");
        log.info("현재  시간 : " + currentTime);
        log.info("season name : " + insertSeasonDto.getSeasonName());
        log.info("season start time : " + insertSeasonDto.getSeasonStartTime());
        log.info("season end time : " + insertSeasonDto.getSeasonEndTime());

        Long seasonNum = schedulerService.createSeason(insertSeasonDto);

        log.info("season Num : " + seasonNum);

        schedulerService.createUserSeason(seasonNum);

    }


    /*
    * 스케쥴러 테스트
    */

    private Clock clock = Clock.systemDefaultZone();

    //     Test - 완료
    @Scheduled(cron = "5,10,15,20 0 12 1 12 *")
    public void test1Scheduler() {

        LocalDateTime startTime = LocalDateTime.now(clock);
        LocalDateTime endTime = startTime.plusMonths(3).minusSeconds(1);

        System.out.println("테스트 실행");
        System.out.println("현재  시간 : " + startTime);
        System.out.println("현재  시간 : " + endTime);

    }

    //     Test - 완료
    public void test2Scheduler() {

        LocalDateTime currentTime = LocalDateTime.now(clock);
        System.out.println("테스트 실행");
        System.out.println("현재  시간 : " + currentTime);

    }

    // 테스트 목적으로 다른 Clock 설정하기
    public void setClock(Clock clock) {
        this.clock = clock;
    }
}

package app.codingGround.api;

import app.codingGround.api.schedule.controller.SchedulerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@SpringBootTest
public class TestClass {

    @Autowired
    private SchedulerController schedulerController;

    @Test
    public void testScheduled() {

        // 원하는 특정 시간으로 Clock 설정
        Clock fixedClock = Clock.fixed(Instant.parse("2023-10-01T00:00:00Z"), ZoneId.of("UTC"));
        schedulerController.setClock(fixedClock);

        // 스케쥴러 메소드 호출
        schedulerController.test1Scheduler();

    }
}

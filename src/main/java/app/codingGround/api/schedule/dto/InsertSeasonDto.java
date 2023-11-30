package app.codingGround.api.schedule.dto;

import lombok.Getter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


@Getter
public class InsertSeasonDto {

    private Long seasonNum;
    private String seasonName;
    private ZonedDateTime seasonStartTime;
    private Timestamp seasonEndTime;

    public InsertSeasonDto(Long seasonNum, String seasonName, Timestamp seasonStartTime, Timestamp seasonEndTime) {
        Date date = new Date();

//
//        this.seasonNum = seasonNum;
//
//        if (month.equals("1")) {
//            this.seasonName = year + "-1";
//        } else if (month.equals("4")) {
//            this.seasonName = year + "-2";
//        } else if (month.equals("7")) {
//            this.seasonName = year + "-3";
//        } else if (month.equals("10")) {
//            this.seasonName = year + "-4";
//        }
    }
}

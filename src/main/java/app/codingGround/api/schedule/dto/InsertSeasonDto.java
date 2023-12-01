package app.codingGround.api.schedule.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class InsertSeasonDto {

    private String seasonName;
    private Timestamp seasonStartTime;
    private Timestamp seasonEndTime;

    public InsertSeasonDto() {

        LocalDateTime nowDate = LocalDateTime.now();

        DateTimeFormatter dateFormatYear =  DateTimeFormatter.ofPattern("yyyy");
        String year = dateFormatYear.format(nowDate);

        DateTimeFormatter dateFormatMonth = DateTimeFormatter.ofPattern("MM");
        String month = dateFormatMonth.format(nowDate);

        if (month.equals("1")) {
            this.seasonName = year + "-1";
        } else if (month.equals("4")) {
            this.seasonName = year + "-2";
        } else if (month.equals("7")) {
            this.seasonName = year + "-3";
        } else if (month.equals("10")) {
            this.seasonName = year + "-4";
        } else if (month.equals("12")) {
            this.seasonName = year + "-5";
        }

        this.seasonStartTime = Timestamp.valueOf(nowDate);
        this.seasonEndTime = Timestamp.valueOf(nowDate.plusMonths(3).minusSeconds(1));
    }
}

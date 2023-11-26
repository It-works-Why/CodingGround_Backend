package app.codingGround.api.admin.dto;

import app.codingGround.api.entity.Notice;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Getter
@ToString
public class NoticeListDto {

    private Long noticeNum;
    private String noticeTitle;
    private String noticeContent;
    private String userNickname;
    private String userProfileImg;
    private String noticeTime;

    public NoticeListDto(Notice notice) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        this.noticeNum = notice.getNoticeNum();
        this.noticeTitle = notice.getNoticeTitle();
        this.noticeContent = notice.getNoticeContent();
        this.userNickname = notice.getUser().getUserNickname();
        this.userProfileImg = notice.getUser().getUserProfileImg();
        this.noticeTime = format.format(notice.getNoticeTime());
    }

}

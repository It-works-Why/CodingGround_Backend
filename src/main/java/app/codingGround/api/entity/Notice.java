package app.codingGround.api.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_NOTICE")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_NUM", nullable = false)
    private Long noticeNum;

    @ManyToOne
    @JoinColumn(name = "USER_NUM", nullable = false)
    private User user;

    @Column(name = "NOTICE_TITLE", length = 50, nullable = false)
    private String noticeTitle;

    @Column(name = "NOTICE_CONTENT", length = 5000, nullable = false)
    private String noticeContent;

    @Column(name = "NOTICE_TIME", nullable = false)
    private Timestamp noticeTime;

    @Column(name = "USE_STATUS", nullable = false)
    private int useStatus;

    // Default 값 입력
    @PrePersist
    protected void onCreate() {
        noticeTime = new Timestamp(new Date().getTime());
        useStatus = 1;
    }
}
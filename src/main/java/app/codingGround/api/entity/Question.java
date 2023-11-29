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
@Table(name = "TB_QUESTION")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_NUM", nullable = false)
    private Long questionNum;

    @Column(name = "QUESTION_TITLE", length = 50, nullable = false)
    private String questionTitle;

    @Column(name = "QUESTION_CONTENT", nullable = false)
    private String questionContent;

    @Column(name = "QUESTION_DIFFICULT", nullable = false)
    private int questionDifficult;

    @Column(name = "QUESTION_REGDATE", nullable = false)
    private Timestamp questionRegdate;

    @Column(name = "USE_STATUS", nullable = false)
    private int useStatus;

    @Column(name = "QUESTION_LIMIT_TIME", nullable = false)
    private int questionLimitTime;

    // Default 값 입력
    @PrePersist
    protected void onCreate() {
        questionRegdate = new Timestamp(new Date().getTime());
        useStatus = 1;
    }
}

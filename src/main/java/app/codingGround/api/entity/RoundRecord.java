package app.codingGround.api.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "TB_ROUND_RECORD")
public class RoundRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROUND_RECORD_NUM")
    private Long roundRecordNum;

    @ManyToOne
    @JoinColumn(name = "ROUND_NUM")
    private Round roundNum;

    @ManyToOne
    @JoinColumn(name = "USER_NUM")
    private User userNum;

    @Column(name = "ROUND_RECORD_ANSWER")
    private String roundRecordAnswer;

    @Column(name = "ROUND_RECORD_ANSWER_CORRECT")
    private int roundRecordAnswerCorrect;

    @Column(name = "ROUND_RECORD_RANKING")
    private String roundRecordRanking;

    @Column(name = "ROUND_RECORD_SUBMIT_TIME")
    private Timestamp roundRecordSubmitTime;

    @Column(name = "ROUND_RECORD_MEMORY")
    private Integer roundRecordMemory;

    @Column(name = "ROUND_RECORD_TOKEN")
    private String roundRecordToken;
}

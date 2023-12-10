package app.codingGround.api.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TB_ROUND")
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROUND_NUM")
    private Long roundNum;

    @ManyToOne
    @JoinColumn(name = "GAME_NUM")
    private Game gameNum;

    @ManyToOne
    @JoinColumn(name = "QUESTION_NUM")
    private Question questionNum;

    @ManyToOne
    @JoinColumn(name = "RULE_NUM")
    private GameRule gameRule;

    @Column(name = "ROUND")
    private int round;

}

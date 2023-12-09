package app.codingGround.api.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TB_GAME_RULE")
public class GameRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RULE_NUM")
    private long ruleNum;

    @Column(name = "RULE_WIN_CONDITION")
    private String ruleWinCondition;

    @Column(name = "RULE_TITLE")
    private String ruleTitle;
}

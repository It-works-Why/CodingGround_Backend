package app.codingGround.api.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_GAME_RECORD")
public class GameRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GAME_RECORD_NUM")
    private Long gameRecordNum;

    @ManyToOne
    @JoinColumn(name = "USER_NUM")
    private User user;

  /*  @ManyToOne*/
    @JoinColumn(name = "GAME_NUM")
    private Integer game;

    @Column(name = "GAME_RECORD",nullable = false)
    private int gameRecord;


    @Column(name = "CHANGE_SCORE", nullable = false)
    private int ChangeScore;
}

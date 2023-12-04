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
@Table(name = "TB_GAME")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GAME_NUM")
    private Long gameNum;

    @ManyToOne
    @JoinColumn(name = "LANGUAGE_NUM")
    private Language languageNum;

    @ManyToOne
    @JoinColumn(name = "SEASON_NUM")
    private Season seasonNum;

    @Column(name = "GAME_DATE",nullable = false)
    private Timestamp gameDate;


    @Column(name = "GAME_TYPE", nullable = false)
    private String gameType;

    @PrePersist
    protected void onCreate() {
        gameDate = new Timestamp(new Date().getTime());
    }
}

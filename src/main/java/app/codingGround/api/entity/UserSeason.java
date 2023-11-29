package app.codingGround.api.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_USER_SEASON")
public class UserSeason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_SEASON_NUM")
    private Long userSeasonNum;

    @ManyToOne
    @JoinColumn(name = "USER_NUM")
    private User user;

    @ManyToOne
    @JoinColumn(name = "RANK_NUM")
    private Rank rankNum;

    @ManyToOne
    @JoinColumn(name = "SEASON_NUM")
    private Rank seasonNum;

    @Column(name = "RANK_SCORE", nullable = false)
    private int rankScore;


}

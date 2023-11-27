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
@Table(name = "TB_SEASON")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEASON_NAME")
    private Long seasonName;

    @ManyToOne
    @JoinColumn(name = "USER_NUM")
    private User user;

    @ManyToOne
    @JoinColumn(name = "RANK_NUM")
    private Rank rankNum;

    @Column(name = "RANK_SCORE", nullable = false)
    private int rankScore;
}

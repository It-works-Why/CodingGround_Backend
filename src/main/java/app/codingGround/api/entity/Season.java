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
    @Column(name = "SEASON_NUM")
    private Long seasonNum;

    @Column(name = "SEASON_NAME", length = 20, nullable = false)
    private String seasonName;

    @Column(name = "SEASON_START_TIME", nullable = false)
    private Timestamp seasonStartTime;

    @Column(name = "SEASON_END_TIME", nullable = false)
    private Timestamp seasonEndTime;

}

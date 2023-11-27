package app.codingGround.api.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_RANK")
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RANK_NUM")
    private Long rankNum;

    @Column(name = "RANK_NAME", length = 20, nullable = false)
    private String rankName;


    @Column(name = "RANK_STANDARD_SCORE", nullable = false)
    private int rankStandardScore;
}

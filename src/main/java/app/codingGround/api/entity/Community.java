package app.codingGround.api.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_COMMUNITY")
@Setter

public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_NUM")
    private Long postNum;

    @ManyToOne
    @JoinColumn(name = "USER_NUM")
    private User user;

    @Column(name = "POST_TITLE", length = 50, nullable = false)
    private String postTitle;

    @Column(name = "POST_CONTENT", length = 5000, nullable = false)
    private String postContent;

    @Column(name = "POST_TIME", nullable = false)
    private Timestamp postTime;

    @Column(name = "USE_STATUS", nullable = false)
    private int useStatus;

    @PrePersist
    protected void onCreate() {
        postTime = new Timestamp(new Date().getTime());
        useStatus = 1;
    }
}

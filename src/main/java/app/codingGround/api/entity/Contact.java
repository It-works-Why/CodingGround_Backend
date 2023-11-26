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
@Table(name = "TB_CONTACT")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTACT_NUM")
    private Long contactNum;

    @ManyToOne
    @JoinColumn(name = "USER_NUM")
    private User user;

    @Column(name = "CONTACT_TITLE", length = 50, nullable = false)
    private String contactTitle;

    @Column(name = "CONTACT_CONTENT", length = 5000, nullable = false)
    private String contactContent;

    @Column(name = "CONTACT_TIME", nullable = false)
    private Timestamp contactTime;

    @Column(name = "CONTACT_ANSWER", length = 5000)
    private String contactAnswer;

    @Column(name = "USE_STATUS", nullable = false)
    private int useStatus;

    @PrePersist
    protected void onCreate() {
        contactTime = new Timestamp(new Date().getTime());
        useStatus = 1;
    }
}

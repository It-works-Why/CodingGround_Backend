package app.codingGround.api.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_TESTCASE")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TESTCASE_NUM", nullable = false)
    private Long testCaseNum;

    @ManyToOne
    @JoinColumn(name = "QUESTION_NUM", nullable = false)
    private Question question;

    @Column(name = "TESTCASE_INPUT", length = 100, nullable = false)
    private String testCaseInput;

    @Column(name = "TESTCASE_OUTPUT", length = 100, nullable = false)
    private String testCaseOutput;
}

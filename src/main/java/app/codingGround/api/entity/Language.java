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
@Table(name = "TB_LANGUAGE")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LANGUAGE_NUM")
    private Long languageNum;


    @Column(name = "LANGUAGE_NAME",length = 10 ,nullable = false)
    private String languageName;


    @Column(name = "QUESTION_BASECODE", nullable = false)
    private String questionBasecode;

}
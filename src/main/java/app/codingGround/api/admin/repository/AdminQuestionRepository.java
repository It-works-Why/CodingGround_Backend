package app.codingGround.api.admin.repository;

import app.codingGround.api.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminQuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAllByUseStatus(Pageable pageable, int useStatus);

    Page<Question> findAllByQuestionTitleContainingAndUseStatus(Pageable pageable, String keyword, int useStatus);

    Question findByQuestionNumAndUseStatus(Long questionNum, int useStatus);

    @Query(value = "SELECT * FROM TB_QUESTION WHERE USE_STATUS = 1 ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Question findRandomRow();
}

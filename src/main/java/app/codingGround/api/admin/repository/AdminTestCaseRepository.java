package app.codingGround.api.admin.repository;

import app.codingGround.api.entity.Question;
import app.codingGround.api.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminTestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findAllByQuestion_UseStatusAndQuestion_QuestionNum(int useStatus, Long questionNum);
}

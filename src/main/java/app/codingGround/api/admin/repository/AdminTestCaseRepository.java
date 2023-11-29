package app.codingGround.api.admin.repository;

import app.codingGround.api.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTestCaseRepository extends JpaRepository<TestCase, Long> {

}

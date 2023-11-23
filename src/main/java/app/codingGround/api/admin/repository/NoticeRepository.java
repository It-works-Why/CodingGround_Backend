package app.codingGround.api.admin.repository;

import app.codingGround.api.admin.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    //
}

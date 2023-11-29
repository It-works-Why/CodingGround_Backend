package app.codingGround.api.admin.repository;

import app.codingGround.api.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNoticeRepository extends JpaRepository<Notice, Long> {
    Notice findByNoticeNumAndUseStatus(Long noticeNum, int useStatus);

    Page<Notice> findAllByUseStatus(Pageable pageable, int useStatus);
}

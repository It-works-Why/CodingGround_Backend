package app.codingGround.api.admin.repository;

import app.codingGround.api.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findByNoticeNum(Long noticeNum);

    //
}
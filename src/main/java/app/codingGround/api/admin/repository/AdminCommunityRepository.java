package app.codingGround.api.admin.repository;

import app.codingGround.api.entity.Community;
import app.codingGround.api.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCommunityRepository extends JpaRepository<Community, Long> {
    Community findByPostNumAndUseStatus(Long postNum, int useStatus);

    Page<Community> findAllByUseStatus(Pageable pageable, int useStatus);


    Page<Community> findAllByUseStatusAndPostTitle(Pageable pageable, int i, String keyword);

    Page<Community> findAllByUseStatusAndUser_UserNickname(Pageable pageable, int i, String keyword);

    Page<Community> findAllByUseStatusAndPostContent(Pageable pageable, int i, String keyword);
}

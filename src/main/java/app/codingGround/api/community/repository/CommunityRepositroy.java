package app.codingGround.api.community.repository;

import app.codingGround.api.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepositroy extends JpaRepository<Community, Long> {
    Community findByPostNumAndUseStatus(Long postNum, int useStatus);

    Page<Community> findAllByUseStatus(Pageable pageable, int useStatuts);

    Community findByPostNum(Long postNum);

    Page<Community> findAllByPostContentContainingAndUseStatus(Pageable pageable, String searchInput, int useStatus);
}

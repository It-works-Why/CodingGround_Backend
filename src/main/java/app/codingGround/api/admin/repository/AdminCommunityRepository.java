package app.codingGround.api.admin.repository;

import app.codingGround.api.entity.Community;
import app.codingGround.api.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCommunityRepository extends JpaRepository<Community, Long> {
    /*Community findByCommunityNumAndUseStatus(Long community, int useStatus);*/

    Page<Community> findAllByUseStatus(Pageable pageable, int useStatus);
}

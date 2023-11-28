package app.codingGround.api.account.repository;

import app.codingGround.api.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepositroy extends JpaRepository<Community, Long> {
    Community findByPostNum(Long postNum);

    Page<Community> findAll(Pageable pageable);
}

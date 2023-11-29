package app.codingGround.api.community.repository;

import app.codingGround.api.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepositroy extends JpaRepository<Community, Long> {
    Community findByPostNum(Long postNum);
}

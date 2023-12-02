package app.codingGround.api.schedule.repository;

import app.codingGround.api.entity.UserSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSeasonRepository extends JpaRepository<UserSeason, Long> {
}
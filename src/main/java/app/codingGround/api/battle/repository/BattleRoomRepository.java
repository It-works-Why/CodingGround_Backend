package app.codingGround.api.battle.repository;

import app.codingGround.api.entity.Language;
import app.codingGround.api.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRoomRepository extends JpaRepository<Language, Long> {

}

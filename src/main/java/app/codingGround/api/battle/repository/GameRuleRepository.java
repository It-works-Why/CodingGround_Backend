package app.codingGround.api.battle.repository;

import app.codingGround.api.entity.GameRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRuleRepository extends JpaRepository<GameRule, Long> {
    GameRule findByRuleNum(Long ruleNum);
}

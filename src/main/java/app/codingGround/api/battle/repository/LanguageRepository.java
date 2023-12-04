package app.codingGround.api.battle.repository;

import app.codingGround.api.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    Language findByLanguageCode(Integer gameLanguage);
}

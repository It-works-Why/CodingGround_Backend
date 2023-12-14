package app.codingGround.api.account.repository;

import app.codingGround.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.config.web.servlet.oauth2.resourceserver.OpaqueTokenDsl;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String username);


    boolean existsByUserId(String userId);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserNickname(String userNickname);

    List<User> findAllByUserStatus(String userStaus);

    User findByUserEmail(String email);

    Optional<User> findByUserNickname(String userNickname);

    User findByUserEmailAndUserId(String userEmail, String userId);
}

package app.codingGround.api.account.repository;

import app.codingGround.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String username);

    boolean existsByUserId(String userId);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserNickname(String userNickname);

    List<User> findAllByUserStatus(String userStaus);

    User findByUserEmailAndUserStatus(String email, String userStatus);
}

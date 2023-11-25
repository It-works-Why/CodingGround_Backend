package app.codingGround.api.user.repository;

import app.codingGround.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String id);

    //
}
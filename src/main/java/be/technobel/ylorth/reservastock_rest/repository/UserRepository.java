package be.technobel.ylorth.reservastock_rest.repository;


import be.technobel.ylorth.reservastock_rest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
    Optional<User> findByLogin(String login);
}

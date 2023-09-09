package be.technobel.ylorth.reservastock_rest.dal.repository;


import be.technobel.ylorth.reservastock_rest.dal.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);
    @Query("Select u from UserEntity u where u.enabled=false")
    List<UserEntity> getAllUnvalidate();
}

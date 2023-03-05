package be.technobel.ylorth.reservastock_rest.repository;

import be.technobel.ylorth.reservastock_rest.model.entity.ResetValidator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ResetValidatorRepository extends JpaRepository<ResetValidator, Long> {

    boolean existsByLogin(String login);
    ResetValidator findByLogin(String login);
}

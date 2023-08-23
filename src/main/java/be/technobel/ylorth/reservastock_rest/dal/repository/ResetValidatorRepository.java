package be.technobel.ylorth.reservastock_rest.dal.repository;

import be.technobel.ylorth.reservastock_rest.dal.models.ResetValidator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetValidatorRepository extends JpaRepository<ResetValidator, Long> {

    boolean existsByLogin(String login);
    ResetValidator findByLogin(String login);
}

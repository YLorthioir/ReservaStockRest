package be.technobel.ylorth.reservastock_rest.dal.repository;

import be.technobel.ylorth.reservastock_rest.dal.models.ResetValidatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetValidatorRepository extends JpaRepository<ResetValidatorEntity, Long> {

    boolean existsByLogin(String login);
    ResetValidatorEntity findByLogin(String login);
}

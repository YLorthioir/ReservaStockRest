package be.technobel.ylorth.reservastock_rest.dal.repository;

import be.technobel.ylorth.reservastock_rest.dal.models.Adress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdressRepository extends JpaRepository<Adress, Long> {
}

package be.technobel.ylorth.reservastock_rest.repository;

import be.technobel.ylorth.reservastock_rest.model.entity.Adress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdressRepository extends JpaRepository<Adress, Long> {
}

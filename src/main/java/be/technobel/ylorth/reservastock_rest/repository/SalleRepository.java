package be.technobel.ylorth.reservastock_rest.repository;


import be.technobel.ylorth.reservastock_rest.model.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SalleRepository extends JpaRepository<Salle,Long> {

    Set<Salle> findAllByCapacite(int capacite);
}

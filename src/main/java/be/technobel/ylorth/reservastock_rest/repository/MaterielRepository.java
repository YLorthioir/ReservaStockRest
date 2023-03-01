package be.technobel.ylorth.reservastock_rest.repository;


import be.technobel.ylorth.reservastock_rest.model.entity.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterielRepository extends JpaRepository<Materiel,Long> {
}

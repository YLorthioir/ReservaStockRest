package be.technobel.ylorth.reservastock_rest.dal.repository;


import be.technobel.ylorth.reservastock_rest.dal.models.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialEntity,Long> {
}

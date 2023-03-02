package be.technobel.ylorth.reservastock_rest.repository;

import be.technobel.ylorth.reservastock_rest.model.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande,Long> {
    @Query("Select d from Demande d where d.admin=null and d.raisonRefus=null")
    public List<Demande> getAllUnconfirmed();
}

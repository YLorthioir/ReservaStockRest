package be.technobel.ylorth.reservastock_rest.dal.repository;

import be.technobel.ylorth.reservastock_rest.dal.models.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestEntity,Long> {
    @Query("Select r from Request r where r.admin=null and r.refusalReason=null")
    List<RequestEntity> getAllUnconfirmed();
}

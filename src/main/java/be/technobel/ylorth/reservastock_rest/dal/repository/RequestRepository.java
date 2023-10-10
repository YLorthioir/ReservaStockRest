package be.technobel.ylorth.reservastock_rest.dal.repository;

import be.technobel.ylorth.reservastock_rest.dal.models.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestEntity,Long>, JpaSpecificationExecutor<RequestEntity> {

    //@Query(nativeQuery = true, value = "Select r from Request r where r.admin_id=null and r.refusal_reason=null")
    //List<RequestEntity> getAllUnconfirmed();
}

package be.technobel.ylorth.reservastock_rest.bll.service;




import be.technobel.ylorth.reservastock_rest.dal.models.MaterialEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface MaterialService {
    List<MaterialEntity> getAll();
    MaterialEntity getOne(Long id);
    void insert(String nom);

    HttpStatus delete(long id);

}

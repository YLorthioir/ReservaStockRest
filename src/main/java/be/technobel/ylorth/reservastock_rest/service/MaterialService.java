package be.technobel.ylorth.reservastock_rest.service;




import be.technobel.ylorth.reservastock_rest.model.dto.MaterialDTO;

import java.util.List;

public interface MaterialService {
    List<MaterialDTO> getAll();
    MaterialDTO getOne(Long id);
    void insert(String nom);

    void delete(long id);

}

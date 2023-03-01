package be.technobel.ylorth.reservastock_rest.service;




import be.technobel.ylorth.reservastock_rest.model.dto.MaterielDTO;

import java.util.List;

public interface MaterielService {
    List<MaterielDTO> getAll();
    MaterielDTO getOne(Long id);
    void insert(String nom);

    void delete(long id);

}

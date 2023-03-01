package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.MaterielDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Materiel;
import be.technobel.ylorth.reservastock_rest.service.SalleService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MaterielMapper {

    public MaterielDTO toDTO(Materiel entity){

        if(entity == null)
            return null;

        return MaterielDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .build();
    }

}

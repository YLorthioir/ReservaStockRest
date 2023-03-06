package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.MaterialDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Material;
import org.springframework.stereotype.Service;

@Service
public class MaterialMapper {

    public MaterialDTO toDTO(Material entity){

        if(entity == null)
            return null;

        return MaterialDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public Material toEntity(MaterialDTO dto){
        if(dto==null)
            return null;

        Material entity = new Material();
        entity.setName(dto.getName());

        return entity;
    }

}

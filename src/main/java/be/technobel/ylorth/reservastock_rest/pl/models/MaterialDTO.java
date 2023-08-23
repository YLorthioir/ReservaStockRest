package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.Material;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialDTO {

    private long id;
    private String name;

    public static MaterialDTO fromBLL(Material entity){

        if(entity == null)
            return null;

        return MaterialDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}

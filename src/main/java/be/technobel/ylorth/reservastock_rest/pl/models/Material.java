package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.MaterialEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Material {

    private long id;
    private String name;

    public static Material fromBLL(MaterialEntity entity){

        if(entity == null)
            return null;

        return Material.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}

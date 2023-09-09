package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.RoomEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class Room {
    private long id;
    private int capacity;
    private String name;
    private boolean forStaff;
    private Set<Material> contains;

    public static Room fromBLL(RoomEntity entity){

        if(entity == null)
            return null;

        return Room.builder()
                .id(entity.getId())
                .name(entity.getName())
                .forStaff(entity.isForStaff())
                .capacity(entity.getCapacity())
                .contains(
                        entity.getContains().stream()
                                .map(Material::fromBLL)
                                .collect(Collectors.toSet())
                )

                .build();
    }

}

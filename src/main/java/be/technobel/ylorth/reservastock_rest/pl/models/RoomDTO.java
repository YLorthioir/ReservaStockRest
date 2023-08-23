package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.Room;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class RoomDTO {
    private long id;
    private int capacity;
    private String name;
    private boolean forStaff;
    private Set<MaterialDTO> contains;

    public static RoomDTO fromBLL(Room entity){

        if(entity == null)
            return null;

        return RoomDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .forStaff(entity.isForStaff())
                .capacity(entity.getCapacity())
                .contains(
                        entity.getContains().stream()
                                .map(MaterialDTO::fromBLL)
                                .collect(Collectors.toSet())
                )

                .build();
    }

}

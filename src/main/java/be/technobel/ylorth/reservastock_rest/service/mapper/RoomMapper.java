package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.RoomDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Room;
import be.technobel.ylorth.reservastock_rest.model.form.RoomForm;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RoomMapper {

    MaterialMapper materialMapper;
    RequestMapper requestMapper;

    public RoomMapper(MaterialMapper materialMapper, RequestMapper requestMapper) {
        this.materialMapper = materialMapper;
        this.requestMapper = requestMapper;
    }

    public RoomDTO toDTO(Room entity){

        if(entity == null)
            return null;

        return RoomDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .forStaff(entity.isForStaff())
                .capacity(entity.getCapacity())
                .contains(
                        entity.getContains().stream()
                        .map(materialMapper::toDTO)
                        .collect(Collectors.toSet())
                )

                .build();
    }

    public Room toEntity(RoomForm form){

        if(form == null)
            return null;

        Room room = new Room();

        room.setName(form.getName());
        room.setCapacity(form.getCapacity());
        room.setForStaff(form.isForStaff());

        return room;
    }
}

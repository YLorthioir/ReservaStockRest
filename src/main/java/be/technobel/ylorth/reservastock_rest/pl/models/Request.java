package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.RequestEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class Request {
    private long id;
    private LocalDateTime startTime;
    private String requestReason;
    private String refusalReason;
    private int minutes;

    private UserDTO userDTO;

    private Long adminId;

    private Room room;

    private Set<Material> materials;

    public static Request fromBLL(RequestEntity entity){

        if(entity == null)
            return null;

        Long adminId=null;
        if(entity.getAdmin()!=null)
            adminId=entity.getAdmin().getId();

        return Request.builder()
                .id(entity.getId())
                .startTime(entity.getStartTime())
                .minutes(entity.getMinutes())
                .room(Room.fromBLL(entity.getRoomEntity()))
                .adminId(adminId)
                .userDTO(UserDTO.fromBLL(entity.getUserEntity()))
                .requestReason(entity.getRequestReason())
                .refusalReason(entity.getRefusalReason())
                .materials(
                        entity.getMaterialEntities().stream()
                                .map(Material::fromBLL)
                                .collect(Collectors.toSet()))
                .build();
    }

}

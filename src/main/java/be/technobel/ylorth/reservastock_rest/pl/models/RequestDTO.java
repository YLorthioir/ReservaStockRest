package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.Request;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class RequestDTO {
    private long id;
    private LocalDateTime startTime;
    private String requestReason;
    private String refusalReason;
    private int minutes;

    private UserDTO userDTO;

    private Long adminId;

    private RoomDTO roomDTO;

    private Set<MaterialDTO> materials;

    public static RequestDTO fromBLL(Request entity){

        if(entity == null)
            return null;

        Long adminId=null;
        if(entity.getAdmin()!=null)
            adminId=entity.getAdmin().getId();

        return RequestDTO.builder()
                .id(entity.getId())
                .startTime(entity.getStartTime())
                .minutes(entity.getMinutes())
                .roomDTO(RoomDTO.fromBLL(entity.getRoom()))
                .adminId(adminId)
                .userDTO(UserDTO.fromBLL(entity.getUser()))
                .requestReason(entity.getRequestReason())
                .refusalReason(entity.getRefusalReason())
                .materials(
                        entity.getMaterials().stream()
                                .map(MaterialDTO::fromBLL)
                                .collect(Collectors.toSet()))
                .build();
    }

}

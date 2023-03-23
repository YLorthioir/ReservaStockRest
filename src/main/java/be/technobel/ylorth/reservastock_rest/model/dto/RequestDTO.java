package be.technobel.ylorth.reservastock_rest.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

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

}

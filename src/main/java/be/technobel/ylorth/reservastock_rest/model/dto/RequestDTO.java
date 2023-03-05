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

    private Long userId;

    private Long adminId;

    private Long roomId;

    private Set<MaterialDTO> materials;

}

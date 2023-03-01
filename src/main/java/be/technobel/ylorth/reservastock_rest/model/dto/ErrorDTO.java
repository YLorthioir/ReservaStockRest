package be.technobel.ylorth.reservastock_rest.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDTO {

    private String message;
    private HttpStatus status;
    private LocalDateTime requestMadeAt;
    private String URI;

}

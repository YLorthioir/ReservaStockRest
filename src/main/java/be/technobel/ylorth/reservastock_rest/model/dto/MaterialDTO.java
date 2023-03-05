package be.technobel.ylorth.reservastock_rest.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialDTO {

    private long id;
    private String name;
}

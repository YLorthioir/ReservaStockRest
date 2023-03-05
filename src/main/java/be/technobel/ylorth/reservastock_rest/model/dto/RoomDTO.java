package be.technobel.ylorth.reservastock_rest.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RoomDTO {
    private long id;
    private int capacity;
    private String name;
    private boolean forStaff;
    private Set<MaterialDTO> contains;

}

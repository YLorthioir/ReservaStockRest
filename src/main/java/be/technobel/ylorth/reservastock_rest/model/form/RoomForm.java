package be.technobel.ylorth.reservastock_rest.model.form;

import be.technobel.ylorth.reservastock_rest.model.dto.RequestDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class RoomForm {

    @NotNull
    private int capacity;
    @NotNull
    @Size(min = 4, max = 40)
    private String name;

    boolean forStaff;
    private Set<RequestDTO> reserved;

    private Set<Long> contains = new LinkedHashSet<>();

}

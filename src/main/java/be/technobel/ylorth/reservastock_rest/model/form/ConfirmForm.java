package be.technobel.ylorth.reservastock_rest.model.form;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class ConfirmForm {

    private String refusalReason;
    private boolean valid;
    private Long room;


}

package be.technobel.ylorth.reservastock_rest.model.dto;

import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthDTO {

    private String token;
    private String login;
    private Set<Role> roles;

}

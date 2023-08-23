package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.Role;
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

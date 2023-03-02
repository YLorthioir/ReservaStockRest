package be.technobel.ylorth.reservastock_rest.model.dto;

import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDTO {
    long id;
    private String nom;
    private String prenom;
    private String login;
    private String email;
    private String telephone;
    private String adresse;
    private Set<Role> roles;
}
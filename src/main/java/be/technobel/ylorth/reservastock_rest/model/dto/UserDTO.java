package be.technobel.ylorth.reservastock_rest.model.dto;

import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class UserDTO {

    private long id;
    private String nom;
    private String prenom;
    private String motDePasse;
    private String login;
    private String email;
    private String telephone;
    private String adresse;
    private Role role;
    private Set<DemandeDTO> demandes = new LinkedHashSet<>();

}

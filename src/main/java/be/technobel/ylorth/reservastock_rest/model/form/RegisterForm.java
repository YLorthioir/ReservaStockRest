package be.technobel.ylorth.reservastock_rest.model.form;

import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.validation.constraints.ConfirmPassword;
import be.technobel.ylorth.reservastock_rest.validation.constraints.EmailNotTaken;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;
import java.util.Set;


@Data
@ConfirmPassword
public class RegisterForm {
    @NotNull
    @Size(min = 4, max = 40)
    private String nom;
    @NotNull
    @Size(min = 4, max = 40)
    private String prenom;
    @NotNull
    @Size(min = 4, max = 40)
    private String motDePasse;
    @NotNull
    @Size(min = 4, max = 40)
    private String confirmMotDePasse;
    @NotNull
    @Email
    @EmailNotTaken
    private String email;
    @NotNull
    @Size(min = 8, max = 13)
    private String telephone;
    @NotNull
    @Size(max = 4)
    private String numero;
    @NotNull
    @Size(min = 4, max = 100)
    private String rue;
    @NotNull
    @Min(1000)
    @Max(99999)
    private int codePostal;
    @NotNull
    @Size(min = 4, max = 100)
    private String ville;
    @NotNull
    @Size(min = 4, max = 100)
    private String pays;
    @Past
    private LocalDate dateDeNaissance;
    private Set<Role> roles;

}

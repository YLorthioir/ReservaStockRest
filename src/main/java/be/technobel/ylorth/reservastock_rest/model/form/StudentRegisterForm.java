package be.technobel.ylorth.reservastock_rest.model.form;

import be.technobel.ylorth.reservastock_rest.validation.constraints.ConfirmPassword;
import be.technobel.ylorth.reservastock_rest.validation.constraints.EmailNotTaken;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class StudentRegisterForm {
    @NotNull
    @Size(min = 4, max = 40)
    private String nom;
    @NotNull
    @Size(min = 4, max = 40)
    private String prenom;
    @NotNull
    @Size(min = 4, max = 40)
    private String motDePasse;
/*    @NotNull
    @Size(min = 4, max = 40)
    private String confirmMotDePasse;*/
    @NotNull
    @Email
    @EmailNotTaken
    private String email;
    @NotNull
    @Size(min = 8, max = 13)
    private String telephone;
    @NotNull
    @Size(min = 4, max = 100)
    private String adresse;


}

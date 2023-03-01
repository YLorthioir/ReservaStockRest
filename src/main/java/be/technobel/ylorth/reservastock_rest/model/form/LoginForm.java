package be.technobel.ylorth.reservastock_rest.model.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginForm {
    @NotNull
    @Size(min = 4, max = 40)
    private String login;
    @NotNull
    @Size(min = 4, max = 40)
    private String motDePasse;
}

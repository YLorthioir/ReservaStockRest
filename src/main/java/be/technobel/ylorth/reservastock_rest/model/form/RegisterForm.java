package be.technobel.ylorth.reservastock_rest.model.form;

import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.validation.constraints.ConfirmPassword;
import be.technobel.ylorth.reservastock_rest.validation.constraints.EmailNotTaken;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@ConfirmPassword
public class RegisterForm {
    @NotNull
    @Size(min = 4, max = 40)
    private String lastName;
    @NotNull
    @Size(min = 4, max = 40)
    private String firstName;
    @NotNull
    @Size(min = 4, max = 40)
    private String password;
    @NotNull
    @Size(min = 4, max = 40)
    private String confirmPassword;
    @NotNull
    @Email
    @EmailNotTaken
    private String email;
    @NotNull
    @Size(min = 8, max = 13)
    private String phone;
    @NotNull
    @Size(max = 4)
    private String number;
    @NotNull
    @Size(min = 4, max = 100)
    private String street;
    @NotNull
    @Min(1000)
    @Max(99999)
    private int postCode;
    @NotNull
    @Size(min = 4, max = 100)
    private String city;
    @NotNull
    @Size(min = 4, max = 100)
    private String country;
    @Past
    private LocalDate birthdate;
    private String role;

    public Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        if (role.equals("ADMIN"))
            roles.add(Role.ADMIN);
        if(role.equals("PROFESSOR"))
            roles.add(Role.PROFESSOR);
        if(role.equals("STUDENT"))
            roles.add(Role.STUDENT);
        return roles;
    }
}

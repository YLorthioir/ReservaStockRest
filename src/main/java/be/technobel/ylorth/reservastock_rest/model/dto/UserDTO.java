package be.technobel.ylorth.reservastock_rest.model.dto;

import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserDTO {
    long id;
    private String lastname;
    private String firstname;
    private String login;
    private String email;
    private String phone;
    private AdressDTO adress;
    private Set<Role> roles;
    private LocalDate birthdate;
}

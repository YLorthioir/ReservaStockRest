package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.Role;
import be.technobel.ylorth.reservastock_rest.dal.models.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserDTO {
    long id;
    private String lastName;
    private String firstName;
    private String login;
    private String email;
    private String phone;
    private AdressDTO adress;
    private Set<Role> roles;
    private LocalDate birthdate;

    public static UserDTO fromBLL(User entity){

        if(entity == null)
            return null;

        return UserDTO.builder()
                .id(entity.getId())
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .login(entity.getLogin())
                .roles(entity.getRoles())
                .adress(AdressDTO.fromBLL(entity.getAdress()))
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .birthdate(entity.getBirthdate())
                .build();
    }
}

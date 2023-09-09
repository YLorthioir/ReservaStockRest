package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.Role;
import be.technobel.ylorth.reservastock_rest.dal.models.UserEntity;
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
    private Adress adress;
    private Set<Role> roles;
    private LocalDate birthdate;

    public static UserDTO fromBLL(UserEntity entity){

        if(entity == null)
            return null;

        return UserDTO.builder()
                .id(entity.getId())
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .login(entity.getLogin())
                .roles(entity.getRoles())
                .adress(Adress.fromBLL(entity.getAdressEntity()))
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .birthdate(entity.getBirthdate())
                .build();
    }
}

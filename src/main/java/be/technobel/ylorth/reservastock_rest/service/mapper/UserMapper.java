package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.UserDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Adress;
import be.technobel.ylorth.reservastock_rest.model.entity.User;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    AdressMapper adressMapper;

    public UserMapper(AdressMapper adressMapper) {
        this.adressMapper = adressMapper;
    }


    public UserDTO toUserDTO(User entity){

        if(entity == null)
            return null;

        return UserDTO.builder()
                .id(entity.getId())
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .login(entity.getLogin())
                .roles(entity.getRoles())
                .adress(adressMapper.toDTO(entity.getAdress()))
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .birthdate(entity.getBirthdate())
                .build();
    }

    public User toEntity(RegisterForm form, Adress adress){

        if(form == null)
            return null;

        User user = new User();

        user.setAdress(adress);
        user.setLastName(form.getLastName());
        user.setFirstName(form.getFirstName());
        user.setPassword(form.getPassword());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setBirthdate(form.getBirthdate());

        return user;
    }
}

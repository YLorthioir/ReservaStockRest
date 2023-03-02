package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.AuthDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.UserDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.model.entity.User;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import be.technobel.ylorth.reservastock_rest.model.form.StudentRegisterForm;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserMapper {

    DemandeMapper demandeMapper;

    public UserMapper(DemandeMapper demandeMapper) {
        this.demandeMapper = demandeMapper;
    }


    public UserDTO toUserDTO(User entity){

        if(entity == null)
            return null;

        return UserDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .login(entity.getLogin())
                .roles(entity.getRoles())
                .adresse(entity.getAdresse())
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .build();
    }

    public User toEntity(RegisterForm form){

        if(form == null)
            return null;

        User user = new User();

        user.setAdresse(form.getAdresse());
        user.setNom(form.getNom());
        user.setPrenom(form.getPrenom());
        user.setMotDePasse(form.getMotDePasse());
        user.setEmail(form.getEmail());
        user.setTelephone(form.getTelephone());
        user.setRoles(form.getRoles());

        return user;
    }

    public User toEntity(StudentRegisterForm form){

        if(form == null)
            return null;

        User user = new User();

        user.setAdresse(form.getAdresse());
        user.setNom(form.getNom());
        user.setPrenom(form.getPrenom());
        user.setMotDePasse(form.getMotDePasse());
        user.setEmail(form.getEmail());
        user.setTelephone(form.getTelephone());
        user.getRoles().add(Role.ETUDIANT);

        return user;
    }
}

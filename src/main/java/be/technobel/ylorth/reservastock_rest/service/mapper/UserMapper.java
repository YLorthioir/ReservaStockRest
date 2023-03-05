package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.UserDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Adresse;
import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.model.entity.User;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    DemandeMapper demandeMapper;
    AdresseMapper adresseMapper;

    public UserMapper(DemandeMapper demandeMapper, AdresseMapper adresseMapper) {
        this.demandeMapper = demandeMapper;
        this.adresseMapper = adresseMapper;
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
                .adresse(adresseMapper.toDTO(entity.getAdresse()))
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .dateDeNaissance(entity.getDateDeNaissance())
                .build();
    }

    public User toEntity(RegisterForm form, Adresse adresse){

        if(form == null)
            return null;

        User user = new User();

        user.setAdresse(adresse);
        user.setNom(form.getNom());
        user.setPrenom(form.getPrenom());
        user.setMotDePasse(form.getMotDePasse());
        user.setEmail(form.getEmail());
        user.setTelephone(form.getTelephone());
        user.setDateDeNaissance(form.getDateDeNaissance());

        return user;
    }
}

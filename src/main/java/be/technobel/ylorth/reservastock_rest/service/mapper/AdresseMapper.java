package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.AdresseDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Adresse;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import org.springframework.stereotype.Service;

@Service
public class AdresseMapper {

    public AdresseDTO toDTO(Adresse entity){

        if(entity == null)
            return null;

        return AdresseDTO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .rue(entity.getRue())
                .ville(entity.getVille())
                .codePostal(entity.getCodePostal())
                .pays(entity.getPays())
                .build();
    }
    public Adresse toEntity(RegisterForm form){

        Adresse adresse = new Adresse();
        adresse.setNumero(form.getNumero());
        adresse.setRue(form.getRue());
        adresse.setCodePostal(form.getCodePostal());
        adresse.setVille(form.getVille());
        adresse.setPays(form.getPays());

        return adresse;
    }

}

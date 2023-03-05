package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.AdressDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Adress;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import org.springframework.stereotype.Service;

@Service
public class AdressMapper {

    public AdressDTO toDTO(Adress entity){

        if(entity == null)
            return null;

        return AdressDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .street(entity.getStreet())
                .city(entity.getCity())
                .postCode(entity.getPostCode())
                .country(entity.getCountry())
                .build();
    }
    public Adress toEntity(RegisterForm form){

        Adress adress = new Adress();
        adress.setNumber(form.getNumber());
        adress.setStreet(form.getStreet());
        adress.setPostCode(form.getPostCode());
        adress.setCity(form.getCity());
        adress.setCountry(form.getCountry());

        return adress;
    }

}

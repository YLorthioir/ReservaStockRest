package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.SalleDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Salle;
import be.technobel.ylorth.reservastock_rest.model.form.SalleForm;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SalleMapper {

    MaterielMapper materielMapper;
    DemandeMapper demandeMapper;

    public SalleMapper(MaterielMapper materielMapper, DemandeMapper demandeMapper) {
        this.materielMapper = materielMapper;
        this.demandeMapper = demandeMapper;
    }

    public SalleDTO toDTO(Salle entity){

        if(entity == null)
            return null;

        return SalleDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .pourPersonnel(entity.isPourPersonnel())
                .capacite(entity.getCapacite())
                .contient(
                        entity.getContient().stream()
                        .map(materielMapper::toDTO)
                        .collect(Collectors.toSet())
                )
                .reserve(entity.getReserve().stream()
                        .map(demandeMapper::toDTO)
                        .collect(Collectors.toSet())

                )

                .build();
    }

    public Salle toEntity(SalleForm form){

        if(form == null)
            return null;

        Salle salle = new Salle();

        salle.setNom(form.getNom());
        salle.setCapacite(form.getCapacite());
        salle.setPourPersonnel(form.isPourPersonnel());

        return salle;
    }
}

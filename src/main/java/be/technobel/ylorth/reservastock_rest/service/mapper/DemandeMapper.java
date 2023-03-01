package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Demande;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DemandeMapper {

    MaterielMapper materielMapper;

    public DemandeMapper(MaterielMapper materielMapper) {
        this.materielMapper = materielMapper;
    }

    public DemandeDTO toDTO(Demande entity){

        if(entity == null)
            return null;

        return DemandeDTO.builder()
                .id(entity.getId())
                .creneau(entity.getCreneau())
                .minutes(entity.getMinutes())
                .salle(entity.getSalle())
                .admin(entity.getAdmin())
                .user(entity.getUser())
                .raisonDemande(entity.getRaisonDemande())
                .raisonRefus(entity.getRaisonRefus())
                .materiels(
                        entity.getMateriels().stream()
                        .map(materielMapper::toDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    public Demande demandeToEntity(DemandeForm form){

        if(form == null)
            return null;

        Demande demande = new Demande();

        demande.setCreneau(form.getCreneau());
        demande.setRaisonDemande(form.getRaisonDemande());
        demande.setMinutes(form.getMinutes());

        return demande;
    }

    public Demande confirmToEntity(ConfirmForm form){

        if(form == null)
            return null;

        Demande demande = new Demande();

        demande.setCreneau(form.getCreneau());
        demande.setRaisonDemande(form.getRaisonDemande());
        demande.setMinutes(form.getMinutes());
        demande.setRaisonRefus(form.getRaisonRefus());

        return demande;
    }
}

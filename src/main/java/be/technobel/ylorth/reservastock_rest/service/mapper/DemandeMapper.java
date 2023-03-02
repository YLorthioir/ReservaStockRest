package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Demande;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DemandeMapper {

    private MaterielMapper materielMapper;

    public DemandeMapper(MaterielMapper materielMapper) {
        this.materielMapper = materielMapper;
    }

    public DemandeDTO toDTO(Demande entity){

        if(entity == null)
            return null;

        Long adminId=null;
        if(entity.getAdmin()!=null)
            adminId=entity.getAdmin().getId();

        return DemandeDTO.builder()
                .id(entity.getId())
                .creneau(entity.getCreneau())
                .minutes(entity.getMinutes())
                .salleId(entity.getSalle().getId())
                .adminId(adminId)
                .userId(entity.getUser().getId())
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

}

package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Demande;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;
import be.technobel.ylorth.reservastock_rest.repository.MaterielRepository;
import be.technobel.ylorth.reservastock_rest.repository.SalleRepository;
import be.technobel.ylorth.reservastock_rest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DemandeMapper {

    private MaterielMapper materielMapper;
    private final SalleRepository salleRepository;
    private final UserRepository userRepository;
    private final MaterielRepository materielRepository;

    public DemandeMapper(MaterielMapper materielMapper,
                         SalleRepository salleRepository,
                         UserRepository userRepository,
                         MaterielRepository materielRepository) {
        this.materielMapper = materielMapper;
        this.salleRepository = salleRepository;
        this.userRepository = userRepository;
        this.materielRepository = materielRepository;
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
        demande.setMateriels(form.getMateriels().stream()
                .map(l -> materielRepository.findById(l).get())
                .collect(Collectors.toSet()));

        return demande;
    }

    public Demande toEntity(DemandeDTO demandeDTO){

        if(demandeDTO == null)
            return null;

        Demande entity = new Demande();

        entity.setId(demandeDTO.getId());
        entity.setCreneau(demandeDTO.getCreneau());
        entity.setMinutes(demandeDTO.getMinutes());
        entity.setSalle(salleRepository.findById(demandeDTO.getSalleId()).get());
        entity.setAdmin(userRepository.findById(demandeDTO.getAdminId()).get());
        entity.setUser(userRepository.findById(demandeDTO.getUserId()).get());
        entity.setRaisonDemande(demandeDTO.getRaisonDemande());
        entity.setRaisonRefus(demandeDTO.getRaisonRefus());
        entity.setMateriels(demandeDTO.getMateriels().stream()
                .map(dto -> materielMapper.toEntity(dto))
                .collect(Collectors.toSet())
        );

        return entity;
    }
}

package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.MaterielDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.SalleDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Demande;
import be.technobel.ylorth.reservastock_rest.model.entity.Materiel;
import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.model.entity.Salle;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;
import be.technobel.ylorth.reservastock_rest.repository.DemandeRepository;
import be.technobel.ylorth.reservastock_rest.repository.MaterielRepository;
import be.technobel.ylorth.reservastock_rest.repository.SalleRepository;
import be.technobel.ylorth.reservastock_rest.repository.UserRepository;
import be.technobel.ylorth.reservastock_rest.service.DemandeService;
import be.technobel.ylorth.reservastock_rest.service.mapper.DemandeMapper;
import be.technobel.ylorth.reservastock_rest.service.mapper.SalleMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;
    private final DemandeMapper demandeMapper;
    private final SalleMapper salleMapper;
    private final MaterielRepository materielRepository;
    private final SalleRepository salleRepository;
    private final UserRepository userRepository;

    public DemandeServiceImpl(DemandeRepository demandeRepository, DemandeMapper demandeMapper, SalleMapper salleMapper, MaterielRepository materielRepository, SalleRepository salleRepository,
                              UserRepository userRepository) {
        this.demandeRepository = demandeRepository;
        this.demandeMapper = demandeMapper;
        this.salleMapper = salleMapper;
        this.materielRepository = materielRepository;
        this.salleRepository = salleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<DemandeDTO> getAllUnconfirm() {
        return demandeRepository.findAll().stream()
                .map(demandeMapper::toDTO)
                .filter(d -> d.getRaisonRefus()==null&&d.getAdmin()==null)
                .toList();
    }
    @Override
    public List<DemandeDTO> getAllByUser(Long id){
        return demandeRepository.findAll().stream()
                .map(demandeMapper::toDTO)
                .filter(d -> d.getUser()==userRepository.findById(id).get())
                .toList();
    }

    @Override
    public DemandeDTO getOne(Long id) {
        return demandeRepository.findById(id)
                .map( demandeMapper::toDTO )
                .orElseThrow(() -> new NotFoundException("demande not found"));
    }

    @Override
    public void insert(DemandeForm form) {
        if( form == null )
            throw new IllegalArgumentException("form should not be null");

        Demande entity = demandeMapper.demandeToEntity(form);
        entity.setMateriels(
                new HashSet<>(materielRepository.findAllById(form.getMateriels()))
        );
        entity.setUser(userRepository.findById(form.getUser()).get());
        entity.setSalle(salleRepository.findById(form.getSalle()).get());

        demandeRepository.save(verification(entity));

    }

    @Override
    public void confirm(ConfirmForm form, Long id) {
        Demande entity = demandeRepository.findById(id).get();

        entity = verification(entity);

        if(form.isValide() && entity.getRaisonRefus()==null)
            entity.setAdmin(userRepository.findById(form.getAdmin()).get());
        else if(entity.getRaisonRefus()==null)
            entity.setRaisonRefus(form.getRaisonRefus());

        entity.setSalle(salleRepository.findById(form.getSalle()).get());

        demandeRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if( !demandeRepository.existsById(id) )
            throw new NotFoundException("Demande not found");

        demandeRepository.deleteById(id);
    }

    @Override
    public void update(DemandeForm form, Long id) {
        if( form == null )
            throw new IllegalArgumentException("form should not be null");

        Demande entity = demandeMapper.demandeToEntity(form);
        entity.setMateriels(
                new HashSet<>(materielRepository.findAllById(form.getMateriels()))
        );
        entity.setUser(userRepository.findById(form.getUser()).get());
        entity.setSalle(salleRepository.findById(form.getSalle()).get());

        entity.setId(id);
        demandeRepository.save(verification(entity));
    }

    @Override
    public Demande verification(Demande entity){

        //Liste de salles correspondantes
        Set<Salle> salleConcordantes=salleRepository.findAll().stream()
                .filter(salle ->salle.getCapacite()==entity.getSalle().getCapacite())
                .collect(Collectors.toSet());

        //prof ou non
        if(entity.getUser().getRole()==Role.ETUDIANT){
            salleConcordantes.stream()
                    .filter(salle -> !salle.isPourPersonnel())
                    .collect(Collectors.toSet());
        }

        //retire les salles ou pas materiel nécessaire
        for (Salle salle:salleConcordantes) {
            for (Materiel materiel: entity.getMateriels()) {
                if(!salle.getContient().contains(materiel))
                    salleConcordantes.remove(salle);
            }
        }

        if(entity.getId()>0) {
            salleConcordantes.removeAll(salleConcordantes);
            salleConcordantes.add(entity.getSalle());
        }

        if(salleConcordantes.size()==0)
            entity.setRaisonRefus("Pas de salles contenant le materiel nécessaire");

        //liste des demandes comprenant les salles correspondantes
        List<Demande> demandeDeSalleConcordante = new ArrayList<>();
        Set<Salle> salleNonDispo = new HashSet<>();

        for (Salle salle:salleConcordantes) {

            List<Demande> listeDemande = demandeRepository.findAll().stream()
                    .filter(demande -> salle == demande.getSalle() && demande.getAdmin()!= null && demande.getRaisonRefus()==null)
                    .toList();

            for (Demande demande:listeDemande) {
                if(!(entity.getCreneau().plusMinutes(entity.getMinutes()).isBefore(demande.getCreneau()) || entity.getCreneau().isAfter(demande.getCreneau().plusMinutes(demande.getMinutes())))) {
                    salleNonDispo.add(salle);
                    demandeDeSalleConcordante.add(demande);
                }
            }
        }

        if(salleNonDispo.size()==salleConcordantes.size()){
            for (Demande demande : demandeDeSalleConcordante) {
                if(entity.getUser().getRole() == Role.PROFESSEUR && demande.getUser().getRole() == Role.ETUDIANT) {
                    demande.setAdmin(null);
                    demande.setRaisonRefus("Demande annulée");
                    demandeRepository.save(demande);
                    break;
                }else {
                    entity.setRaisonRefus("Pas de salles disponible");
                    break;
                }
            }
        }

        return entity;
    }

    public List<SalleDTO> listeSalleDispo(DemandeDTO demande){
        List<SalleDTO> listeSalleConcordante =salleRepository.findAll().stream()
                .map(salleMapper::toDTO)
                .filter(salle -> salle.getCapacite()==demande.getSalle().getCapacite())
                .toList();

        //retire les salles ou pas materiel nécessaire
        for (SalleDTO salle: listeSalleConcordante) {
            for (MaterielDTO materiel: demande.getMateriels()) {
                if(!salle.getContient().contains(materiel))
                    listeSalleConcordante.remove(salle);
            }
        }

        if(listeSalleConcordante.size()==0)
            demande.setRaisonRefus("Pas de salles contenant le materiel nécessaire");

        List<Demande> demandeDuJour = demandeRepository.findAll().stream()
                .filter(d -> d.getCreneau().getYear()==demande.getCreneau().getYear())
                .filter(d -> d.getCreneau().getDayOfYear()==demande.getCreneau().getDayOfYear())
                .filter(d -> d.getSalle().getCapacite()==demande.getSalle().getCapacite())
                .filter(d -> d.getAdmin()!=null)
                .toList();

        Set<SalleDTO> listeSalleConcordanteDispo = new HashSet<>();

        for (SalleDTO salle: listeSalleConcordante) {
            for (Demande demandeAcceptee: demandeDuJour) {
                if(demandeAcceptee.getSalle().getId()!= salle.getId() || (demandeAcceptee.getCreneau().isAfter(demande.getCreneau().plusMinutes(demande.getMinutes())) || demandeAcceptee.getCreneau().plusMinutes(demandeAcceptee.getMinutes()).isBefore(demande.getCreneau())))
                    listeSalleConcordanteDispo.add(salle);
            }
        }


        return listeSalleConcordanteDispo.stream().toList();
    }

}

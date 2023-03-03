package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        return demandeRepository.getAllUnconfirmed().stream()
                .map(demandeMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<DemandeDTO> getAllByUser(String username){
        return demandeRepository.findAll().stream()
                .map(demandeMapper::toDTO)
                .filter(d -> d.getUserId()==userRepository.findByLogin(username).get().getId())
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

        entity.setSalle(salleRepository.findById(form.getSalle()).get());
        entity = verification(entity);

        if(form.isValide() && entity.getRaisonRefus()==null)
            entity.setAdmin(userRepository.findById(form.getAdmin()).get());
        else if(entity.getRaisonRefus()==null)
            entity.setRaisonRefus(form.getRaisonRefus());

        entity.setSalle(salleRepository.findById(form.getSalle()).get());

        demandeRepository.save(entity);
    }

    @Override
    public void delete(Long id, Authentication authentication) {
        if( !demandeRepository.existsById(id) )
            throw new NotFoundException("Demande not found");

        if(demandeRepository.findById(id).get().getUser().getLogin().equals(authentication.getPrincipal().toString()) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
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

        Set<Salle> salleConcordantes= sallesCorrespondante(entity);

        //Si demande en traitement (donc a un id), on met la salle choisie
        if(entity.getId()>0) {
            salleConcordantes.removeAll(salleConcordantes);
            salleConcordantes.add(entity.getSalle());
        }

        //si aucun salles ne correspond on signale
        if(salleConcordantes.size()==0)
            entity.setRaisonRefus("Pas de salles contenant le materiel nécessaire");

        //liste des demandes validées comprenant les salles correspondantes au moment de la demande
        List<Demande> demandeDeSalleConcordanteDejaValide = new ArrayList<>();
        //Set de salles non dispo à ce moment
        Set<Salle> salleNonDispo = new HashSet<>();


        for (Salle salle:salleConcordantes) {

            List<Demande> listeDemande = demandeRepository.findAll().stream()
                    .filter(demande -> salle == demande.getSalle() && demande.getAdmin()!= null && demande.getRaisonRefus()==null)
                    .toList();

            for (Demande demande:listeDemande) {
                if(!(entity.getCreneau().plusMinutes(entity.getMinutes()).isBefore(demande.getCreneau()) || entity.getCreneau().isAfter(demande.getCreneau().plusMinutes(demande.getMinutes())))) {
                    salleNonDispo.add(salle);
                    demandeDeSalleConcordanteDejaValide.add(demande);
                }
            }
        }

        //si salleNonDispo < salles concordantes: il y a une salle qui correspond à la demande dispo.
        //si non, on regarde si c'est un prof qui a fait la demande
        //si aucun match, on stipule qu'il n'y a pas de salles dispo
        boolean insertOK = false;

        if(salleNonDispo.size()==salleConcordantes.size() && (entity.getUser().getRoles().contains(Role.PROFESSEUR) || entity.getUser().getRoles().contains(Role.ADMIN))){
            for (Salle salle : salleNonDispo) {

                //Liste des demande qui on la même salle
                List<Demande> demandeValideeParSalle =demandeDeSalleConcordanteDejaValide.stream()
                        .filter(demande -> demande.getSalle()==salle)
                        .toList();

                //recherche des demandes faites par un prof
                List<Demande>  demandeProf= demandeValideeParSalle.stream()
                        .filter(demande -> demande.getUser().getRoles().contains(Role.PROFESSEUR))
                        .toList();

                /*
                si dans les demandes existantes pendant le créneau demandé,
                il n'y a que celles d'étudiants. on les supprimes pour mettre celle du prof
                */

                if(demandeProf.size()==0) {
                    demandeValideeParSalle.stream()
                            .forEach(d -> {
                                d.setRaisonRefus("Demande annulée");
                                demandeRepository.save(d);
                                entity.setRaisonRefus(null);
                            });
                    return entity;
                }
            }
        } else if (salleNonDispo.size()==salleConcordantes.size()) {
            entity.setRaisonRefus("Pas de salles disponible");
        }

        return entity;
    }
    public Set<Salle> sallesCorrespondante(Demande entity){
        //Salles ayant la même capacité

        Set<Salle> salleConcordantes= salleRepository.findAllByCapacite(entity.getSalle().getCapacite());

        //salles selon si demande d'un prof ou non
        if(entity.getUser().getRoles().contains(Role.ETUDIANT)){
            salleConcordantes.stream()
                    .filter(salle -> !salle.isPourPersonnel())
                    .collect(Collectors.toSet());
        }

        //retire les salles où pas materiel nécessaire
        salleConcordantes.stream()
                .filter(salle -> salle.getContient().containsAll(entity.getMateriels()))
                .collect(Collectors.toList());

        return salleConcordantes;
    }
}

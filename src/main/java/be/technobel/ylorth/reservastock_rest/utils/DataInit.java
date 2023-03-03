package be.technobel.ylorth.reservastock_rest.utils;

import be.technobel.ylorth.reservastock_rest.model.entity.*;
import be.technobel.ylorth.reservastock_rest.repository.DemandeRepository;
import be.technobel.ylorth.reservastock_rest.repository.MaterielRepository;
import be.technobel.ylorth.reservastock_rest.repository.SalleRepository;
import be.technobel.ylorth.reservastock_rest.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Log4j2
public class DataInit implements InitializingBean {
    private final DemandeRepository demandeRepository;
    private final UserRepository userRepository;

    private final SalleRepository salleRepository;
    private final MaterielRepository materielRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInit(SalleRepository salleRepository, MaterielRepository materielRepository,
                    UserRepository userRepository,
                    DemandeRepository demandeRepository,
                    PasswordEncoder passwordEncoder) {
        this.salleRepository = salleRepository;
        this.materielRepository = materielRepository;
        this.userRepository = userRepository;
        this.demandeRepository = demandeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        log.info("-- INITIALIZING DB DATA --");

        Materiel materiel1 = new Materiel();
        materiel1.setNom("Tableau interactif");
        materiel1 = materielRepository.save(materiel1);

        Materiel materiel2 = new Materiel();
        materiel2.setNom("Projecteur");
        materiel2 = materielRepository.save(materiel2);

        Materiel materiel3 = new Materiel();
        materiel3.setNom("Télévision");
        materiel3 = materielRepository.save(materiel3);

        Materiel materiel4 = new Materiel();
        materiel4.setNom("Ordinateurs");
        materiel4 = materielRepository.save(materiel4);

        Salle salle1 = new Salle();
        salle1.setNom("Sonic");
        salle1.setCapacite(20);
        salle1.setPourPersonnel(false);
        Set<Materiel> set1 = new LinkedHashSet<>();
        set1.add(materiel1);
        set1.add(materiel2);
        salle1.setContient(set1);

        salleRepository.save(salle1);

        Salle salle2 = new Salle();
        salle2.setNom("Mario");
        salle2.setCapacite(60);
        salle2.setPourPersonnel(true);
        Set<Materiel> set2 = new LinkedHashSet<>();
        set2.add(materiel3);
        set2.add(materiel4);
        salle2.setContient(set2);

        salleRepository.save(salle2);

        Salle salle3 = new Salle();
        salle3.setNom("Sonic 2");
        salle3.setCapacite(20);
        salle3.setPourPersonnel(false);
        Set<Materiel> set3 = new LinkedHashSet<>();
        set3.add(materiel1);
        set3.add(materiel2);
        salle3.setContient(set3);

        salleRepository.save(salle3);

        User user1 = new User();
        Set<Role> setRole1 = new HashSet<>();
        setRole1.add(Role.ETUDIANT);
        user1.setNom("Dupont");
        user1.setPrenom("Jean");
        user1.setRoles(setRole1);
        user1.setTelephone("0123/2344.233");
        user1.setAdresse("rue du blabla 1");
        user1.setDateDeNaissance(LocalDate.now());
        user1.setLogin("login");
        user1.setMotDePasse(passwordEncoder.encode("Test1234="));
        user1.setEmail("test@test.be");
        user1.setActif(true);

        user1 = userRepository.save(user1);

        User user2 = new User();
        Set<Role> setRole2 = new HashSet<>();
        setRole2.add(Role.PROFESSEUR);
        user2.setNom("Marc");
        user2.setPrenom("Pierre");
        user2.setRoles(setRole2);
        user2.setTelephone("0123/234.233");
        user2.setAdresse("rue du blabla 2");
        user2.setDateDeNaissance(LocalDate.of(1981,11,19));
        user2.setLogin("login2");
        user2.setMotDePasse(passwordEncoder.encode("Test1234="));
        user2.setEmail("test@test.Com");
        user2.setActif(true);


        user2 = userRepository.save(user2);

        User user3 = new User();
        Set<Role> setRole3 = new HashSet<>();
        setRole3.add(Role.ADMIN);
        user3.setNom("Lorthioir");
        user3.setPrenom("Yann");
        user3.setRoles(setRole3);
        user3.setTelephone("0123/2333333");
        user3.setAdresse("rue de l'admin 2");
        user3.setDateDeNaissance(LocalDate.of(1991,03,13));
        user3.setLogin("admin");
        user3.setMotDePasse(passwordEncoder.encode("Test1234="));
        user3.setEmail("y_lorthioir@msn.com");
        user3.setActif(true);


        user3 = userRepository.save(user3);

        Demande demande1 = new Demande();
        demande1.setSalle(salle1);
        demande1.setMateriels(set1);
        demande1.setUser(user1);
        demande1.setAdmin(user3);
        demande1.setCreneau(LocalDateTime.now());
        demande1.setMinutes(30);
        demande1.setRaisonDemande("Essai");


        demande1 = demandeRepository.save(demande1);

        Demande demande2 = new Demande();
        demande2.setSalle(salle1);
        demande2.setMateriels(set1);
        demande2.setUser(user1);
        demande2.setCreneau(LocalDateTime.now().plusHours(1));
        demande2.setMinutes(30);
        demande2.setRaisonDemande("Essai2");

        demande2 = demandeRepository.save(demande2);

        Demande demande3 = new Demande();
        demande3.setSalle(salle1);
        demande3.setMateriels(set1);
        demande3.setUser(user1);
        demande3.setCreneau(LocalDateTime.now().plusHours(2));
        demande3.setMinutes(30);
        demande3.setRaisonDemande("Essai3");
        demande3.setRaisonRefus("Refus test");

        demande3 = demandeRepository.save(demande3);

        Demande demande4 = new Demande();
        demande4.setSalle(salle1);
        demande4.setMateriels(set1);
        demande4.setUser(user1);
        demande4.setAdmin(user3);
        demande4.setCreneau(LocalDateTime.now().plusHours(3));
        demande4.setMinutes(30);
        demande4.setRaisonDemande("Essai4");

        demande4 = demandeRepository.save(demande4);

        Demande demande5 = new Demande();
        demande5.setSalle(salle3);
        demande5.setMateriels(set1);
        demande5.setUser(user2);
        demande5.setAdmin(user3);
        demande5.setCreneau(LocalDateTime.now());
        demande5.setMinutes(20);
        demande5.setRaisonDemande("Essai prof salle");

        demande5 = demandeRepository.save(demande5);

        log.info("-- DATA INIT FINISHED --");
    }

}

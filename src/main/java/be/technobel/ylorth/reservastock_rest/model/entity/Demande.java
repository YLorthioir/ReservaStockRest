package be.technobel.ylorth.reservastock_rest.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "demande_id", nullable = false, unique = true)
    private long id;
    @Column(nullable = false)
    private LocalDateTime creneau;
    @Column(nullable = false)
    private int minutes;
    private String raisonDemande;
    private String raisonRefus;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToMany
    @JoinTable(name = "demande_materiels",
            joinColumns = @JoinColumn(name = "demande_id"),
            inverseJoinColumns = @JoinColumn(name = "materiel_id"))
    private Set<Materiel> materiels = new LinkedHashSet<>();

}

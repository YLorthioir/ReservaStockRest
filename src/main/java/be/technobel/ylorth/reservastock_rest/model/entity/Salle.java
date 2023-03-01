package be.technobel.ylorth.reservastock_rest.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "salle_id", nullable = false, unique = true)
    private long id;
    @Column(nullable = false)
    private int capacite;
    @Column(nullable = false, unique = true)
    private String nom;
    @Column(nullable = false)
    private boolean pourPersonnel;
    @OneToMany(mappedBy = "salle")
    private Set<Demande> reserve;

    @ManyToMany
    @JoinTable(name = "salle_contient",
            joinColumns = @JoinColumn(name = "salle_id"),
            inverseJoinColumns = @JoinColumn(name = "materiel_id"))
    private Set<Materiel> contient = new LinkedHashSet<>();

}

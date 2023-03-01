package be.technobel.ylorth.reservastock_rest.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Materiel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "materiel_id", nullable = false, unique = true)
    private long id;
    @Column(nullable = false, unique = true)
    private String nom;

    @ManyToMany(mappedBy = "contient")
    private Set<Salle> salles = new LinkedHashSet<>();

}

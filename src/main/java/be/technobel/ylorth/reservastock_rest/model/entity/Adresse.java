package be.technobel.ylorth.reservastock_rest.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Adresse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "adresse_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String numero;
    @Column(nullable = false)
    private String rue;
    @Column(nullable = false)
    private int codePostal;
    @Column(nullable = false)
    private String ville;
    @Column(nullable = false)
    private String pays;


}

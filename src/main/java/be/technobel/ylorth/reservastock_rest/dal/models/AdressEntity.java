package be.technobel.ylorth.reservastock_rest.dal.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity (name = "Adress")
@Getter @Setter
public class AdressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "adress_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private int postCode;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;


}

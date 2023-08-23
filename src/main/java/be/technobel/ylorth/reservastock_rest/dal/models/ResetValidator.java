package be.technobel.ylorth.reservastock_rest.dal.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ResetValidator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "resetValidator_id", nullable = false, unique = true)
    Long id;
    @Column(nullable = false, unique = true)
    String login;
    @Column(nullable = false)
    LocalDateTime resetTime;
}

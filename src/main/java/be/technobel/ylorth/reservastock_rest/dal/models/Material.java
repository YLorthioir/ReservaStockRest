package be.technobel.ylorth.reservastock_rest.dal.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "material_id", nullable = false, unique = true)
    private long id;
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "contains")
    private Set<Room> rooms = new LinkedHashSet<>();

}

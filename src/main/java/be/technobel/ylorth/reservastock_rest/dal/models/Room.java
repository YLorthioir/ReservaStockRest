package be.technobel.ylorth.reservastock_rest.dal.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_id", nullable = false, unique = true)
    private long id;
    @Column(nullable = false)
    private int capacity;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private boolean forStaff;
    @OneToMany(mappedBy = "room")
    private Set<Request> reserved;

    @ManyToMany
    @JoinTable(name = "room_contains",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id"))
    private Set<Material> contains = new LinkedHashSet<>();

}

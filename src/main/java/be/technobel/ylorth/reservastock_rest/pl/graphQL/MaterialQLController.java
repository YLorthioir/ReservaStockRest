package be.technobel.ylorth.reservastock_rest.pl.graphQL;

import be.technobel.ylorth.reservastock_rest.dal.models.MaterialEntity;
import be.technobel.ylorth.reservastock_rest.dal.repository.MaterialRepository;
import be.technobel.ylorth.reservastock_rest.pl.models.Material;
import be.technobel.ylorth.reservastock_rest.pl.models.Room;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MaterialQLController {
    private final MaterialRepository materialRepository;

    public MaterialQLController(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @QueryMapping
    public Material material(@Argument long id) {
        return Material.fromBLL(this.materialRepository.findById(id).orElseThrow(() -> new RuntimeException("")));
    }

    @MutationMapping
    public MaterialEntity createMaterial(@Argument String name){
        if(name.length()<=5)
            throw new IllegalArgumentException("Taille pas bonne");
        MaterialEntity materialEntity = new MaterialEntity();
        materialEntity.setName(name);
        return materialRepository.save(materialEntity);
    }

    @QueryMapping
    public List<Material> materials() {
        return materialRepository.findAll().stream().map(Material::fromBLL).toList();
    }


}

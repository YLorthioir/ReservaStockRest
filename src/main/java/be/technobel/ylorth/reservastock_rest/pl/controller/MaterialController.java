package be.technobel.ylorth.reservastock_rest.pl.controller;

import be.technobel.ylorth.reservastock_rest.pl.models.Material;
import be.technobel.ylorth.reservastock_rest.bll.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Material>> getAll(){
        return ResponseEntity.ok(materialService.getAll().stream().map(Material::fromBLL).toList());
    }

    @GetMapping("/{id:[0-9]+}")
    public Material getOne(@PathVariable long id){
        return Material.fromBLL(materialService.getOne(id));
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid String nom) {
        materialService.insert(nom);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public HttpStatus delete(@PathVariable long id) {
        return materialService.delete(id);
    }
}

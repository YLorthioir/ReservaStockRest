package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.MaterialDTO;
import be.technobel.ylorth.reservastock_rest.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/all")
    public List<MaterialDTO> getAll(){
        return materialService.getAll();
    }

    @GetMapping("/{id:[0-9]+}")
    public MaterialDTO getOne(@PathVariable long id){
        return materialService.getOne(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid String nom) {
        materialService.insert(nom);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable long id){
        materialService.delete(id);
    }

}

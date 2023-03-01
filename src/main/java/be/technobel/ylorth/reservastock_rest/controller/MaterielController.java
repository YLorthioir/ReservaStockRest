package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.MaterielDTO;
import be.technobel.ylorth.reservastock_rest.service.MaterielService;
import be.technobel.ylorth.reservastock_rest.service.SalleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materiel")
public class MaterielController {

    private final MaterielService materielService;
    private final SalleService salleService;

    public MaterielController(MaterielService materielService,
                              SalleService salleService) {
        this.materielService = materielService;
        this.salleService = salleService;
    }


    @GetMapping("/all")
    public List<MaterielDTO> getAll(){
        return materielService.getAll();
    }

    @GetMapping("/{id:[0-9]+}")
    public MaterielDTO getOne( @PathVariable long id){
        return materielService.getOne(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid String nom) {
        materielService.insert(nom);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable long id){
        materielService.delete(id);
    }

}

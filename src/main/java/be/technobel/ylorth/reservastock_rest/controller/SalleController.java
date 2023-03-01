package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.SalleDTO;
import be.technobel.ylorth.reservastock_rest.model.form.SalleForm;
import be.technobel.ylorth.reservastock_rest.repository.MaterielRepository;
import be.technobel.ylorth.reservastock_rest.service.MaterielService;
import be.technobel.ylorth.reservastock_rest.service.SalleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salle")
public class SalleController {

    private final SalleService salleService;

    public SalleController(SalleService salleService, MaterielService materielService, MaterielRepository materielRepository) {
        this.salleService = salleService;
    }

    @GetMapping("/all")
    public List<SalleDTO> getAll(){
        return salleService.getAll();
    }

    @GetMapping("/{id:[0-9]+}")
    public SalleDTO getOne(@PathVariable long id){
        return salleService.getOne(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid SalleForm form){
        salleService.insert(form);
    }

    @PutMapping("/{id:[0-9]+}")
    public void updateForm(@PathVariable long id, @RequestBody @Valid SalleForm form){
        salleService.update(form,id);
    }
}

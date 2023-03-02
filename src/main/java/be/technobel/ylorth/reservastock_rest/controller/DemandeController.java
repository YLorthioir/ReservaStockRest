package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;
import be.technobel.ylorth.reservastock_rest.service.AuthService;
import be.technobel.ylorth.reservastock_rest.service.DemandeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demande")
public class DemandeController {
    private final DemandeService demandeService;
    private final AuthService authService;


    public DemandeController(DemandeService demandeService, AuthService authService) {
        this.demandeService = demandeService;
        this.authService = authService;
    }

    @GetMapping("/all")
    public List<DemandeDTO> getAllUnconfirm(){
        return demandeService.getAllUnconfirm();
    }

    @GetMapping("/{id:[0-9]+}")
    public DemandeDTO getOne(@PathVariable long id){
        return demandeService.getOne(id);
    }

    @GetMapping("/all/{id:[0-9]+}")
    public List<DemandeDTO> getAllByUser(@PathVariable long id){
        return demandeService.getAllByUser(id);
    }

    @PostMapping("/{id:[0-9]+}/add")
    public void addDemande(@PathVariable Long userId, @RequestBody @Valid DemandeForm form){
        form.setUser(userId);
        demandeService.insert(form);
    }

    @PostMapping("/{id:[0-9]+}/confirm")
    public void processConfirmForm(@PathVariable Long demandeId,@RequestBody @Valid ConfirmForm form, @RequestHeader String loginAdmin){
        form.setAdmin(authService.findByLogin(loginAdmin));
        demandeService.confirm(form, demandeId);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable long id){
        demandeService.delete(id);
    }
}

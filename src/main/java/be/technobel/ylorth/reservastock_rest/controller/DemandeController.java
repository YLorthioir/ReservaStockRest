package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;
import be.technobel.ylorth.reservastock_rest.service.AuthService;
import be.technobel.ylorth.reservastock_rest.service.DemandeService;
import be.technobel.ylorth.reservastock_rest.utils.JwtProvider;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/allUnconfirm")
    public List<DemandeDTO> getAllUnconfirm(){
        return demandeService.getAllUnconfirm();
    }

    @GetMapping("/{id:[0-9]+}")
    public DemandeDTO getOne(@PathVariable long idDemande){
        return demandeService.getOne(idDemande);
    }

    @GetMapping("/all")
    public List<DemandeDTO> getAllByUser(Authentication authentication){
        return demandeService.getAllByUser(authentication.getPrincipal().toString());
    }

    @PostMapping("/add")
    public void addDemande(@RequestBody @Valid DemandeForm form, Authentication authentication){
        form.setUser(authService.findByLogin(authentication.getPrincipal().toString()));
        demandeService.insert(form);
    }

    @PostMapping("/{id:[0-9]+}/confirm")
    public void processConfirmForm(@PathVariable Long idDemande,@RequestBody @Valid ConfirmForm form, Authentication authentication){
        form.setAdmin(authService.findByLogin(authentication.getPrincipal().toString()));
        demandeService.confirm(form, idDemande);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable long idDemande){
        demandeService.delete(idDemande);
    }
}

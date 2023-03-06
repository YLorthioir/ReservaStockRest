package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.RequestDTO;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.RequestForm;
import be.technobel.ylorth.reservastock_rest.service.AuthService;
import be.technobel.ylorth.reservastock_rest.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {
    private final RequestService requestService;
    private final AuthService authService;

    public RequestController(RequestService requestService, AuthService authService) {
        this.requestService = requestService;
        this.authService = authService;
    }

    @GetMapping("/allUnconfirmed")
    public List<RequestDTO> getAllUnconfirmed(){
        return requestService.getAllUnconfirmed();
    }

    @GetMapping("/{id:[0-9]+}")
    public RequestDTO getOne(@PathVariable long id){
        return requestService.getOne(id);
    }

    @GetMapping("/all")
    public List<RequestDTO> getAllByUser(Authentication authentication){
        return requestService.getAllByUser(authentication.getPrincipal().toString());
    }

    @PostMapping("/add")
    public void addRequest(@RequestBody @Valid RequestForm form, Authentication authentication){
        requestService.insert(form, authentication);
    }

    @PatchMapping("/{id:[0-9]+}/confirm")
    public void processConfirmForm(@PathVariable Long id,@RequestBody @Valid ConfirmForm form, Authentication authentication){
        if(form.isValid()) {
            form.setAdmin(authService.findByLogin(authentication.getPrincipal().toString()));
            form.setRefusalReason(null);
        }else{
            form.setAdmin(null);
        }
        requestService.confirm(form, id);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable long id, Authentication authentication){
        requestService.delete(id, authentication);
    }
}

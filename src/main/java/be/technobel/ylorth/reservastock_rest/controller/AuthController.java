package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.AuthDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.UserDTO;
import be.technobel.ylorth.reservastock_rest.model.form.LoginForm;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import be.technobel.ylorth.reservastock_rest.model.form.StudentRegisterForm;
import be.technobel.ylorth.reservastock_rest.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthDTO login(@RequestBody @Valid LoginForm form){
        return authService.login(form);
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterForm form){
        authService.register(form);
    }
    @PostMapping("/studentRegister")
    public void registerStudent(@RequestBody @Valid StudentRegisterForm form){
        authService.register(form);
    }

    @GetMapping("/toValidate")
    public List<UserDTO> toValidate(){
        return authService.getAllUnvalidate();
    }
    @PostMapping("/validate/{id:[0-9]+}")
    public void validate(@PathVariable long id){
        authService.validate(id);
    }
}

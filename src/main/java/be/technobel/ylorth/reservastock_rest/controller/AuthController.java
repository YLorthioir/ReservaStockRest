package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.AuthDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.model.form.LoginForm;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import be.technobel.ylorth.reservastock_rest.model.form.StudentRegisterForm;
import be.technobel.ylorth.reservastock_rest.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public AuthDTO registerStudent(@RequestBody @Valid RegisterForm form){
        return authService.register(form);
    }
    @PostMapping("/studentregister")
    public AuthDTO registerStudent(@RequestBody @Valid StudentRegisterForm form){
        return authService.register(form);
    }

}

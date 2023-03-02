package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.AuthDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.UserDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Role;
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
    public AuthDTO register(@RequestBody @Valid RegisterForm form){
        return authService.register(form);
    }
    @PostMapping("/studentRegister")
    public AuthDTO registerStudent(@RequestBody @Valid StudentRegisterForm form){
        return authService.register(form);
    }

    @GetMapping("/toValidate")
    public List<UserDTO> toValidate(){
        return authService.getAllUnvalidate();
    }
    @PostMapping("/{id:[0-9]+}")
    public void validate(@PathVariable long id){
        authService.validate(id);
    }
}

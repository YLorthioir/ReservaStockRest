package be.technobel.ylorth.reservastock_rest.pl.controller;

import be.technobel.ylorth.reservastock_rest.pl.models.Auth;
import be.technobel.ylorth.reservastock_rest.pl.models.UserDTO;
import be.technobel.ylorth.reservastock_rest.pl.models.LoginForm;
import be.technobel.ylorth.reservastock_rest.pl.models.RegisterForm;
import be.technobel.ylorth.reservastock_rest.bll.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Auth> login(@RequestBody @Valid LoginForm form){
        return ResponseEntity.ok(authService.login(form));
    }
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterForm form){
        authService.register(form);
    }
    @PostMapping("/studentRegister")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerStudent(@RequestBody @Valid RegisterForm form){
        authService.registerStudent(form);
    }
    @GetMapping("/toValidate")
    public List<UserDTO> toValidate(){
        return authService.getAllUnvalidate().stream().map(UserDTO::fromBLL).toList();
    }
    @PostMapping("/validate/{id:[0-9]+}")
    public void validate(@PathVariable long id){
        authService.validate(id);
    }
    @PostMapping("/unValidate/{id:[0-9]+}")
    public void unValidate(@PathVariable long id){
        authService.unValidate(id);
    }
    @PostMapping("/sendNewPasswordRequest")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendPasswordMail(@RequestBody @Valid String login){
        authService.sendPasswordMail(login);
    }
    @PostMapping("/newPassword")
    @ResponseStatus(HttpStatus.CREATED)
    public void newPassword(@RequestBody String password, @RequestParam String email){
        authService.resetPassword(password, email);
    }
}

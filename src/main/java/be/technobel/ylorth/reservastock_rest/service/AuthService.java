package be.technobel.ylorth.reservastock_rest.service;

import be.technobel.ylorth.reservastock_rest.model.dto.AuthDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.UserDTO;
import be.technobel.ylorth.reservastock_rest.model.form.LoginForm;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;

import java.util.List;

public interface AuthService {
    void register(RegisterForm form);
    void registerStudent(RegisterForm form);
    boolean checkEmailNotTaken(String email);
    AuthDTO login(LoginForm form);
    Long findByLogin(String login);
    void validate(Long id);
    void unValidate(Long id);
    List<UserDTO>  getAllUnvalidate();
    void sendPassword(String login);

}

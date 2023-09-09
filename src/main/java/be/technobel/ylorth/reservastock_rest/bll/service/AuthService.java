package be.technobel.ylorth.reservastock_rest.bll.service;

import be.technobel.ylorth.reservastock_rest.dal.models.UserEntity;
import be.technobel.ylorth.reservastock_rest.pl.models.Auth;
import be.technobel.ylorth.reservastock_rest.pl.models.LoginForm;
import be.technobel.ylorth.reservastock_rest.pl.models.RegisterForm;

import java.util.List;

public interface AuthService {
    void register(RegisterForm form);
    void registerStudent(RegisterForm form);
    boolean checkEmailNotTaken(String email);
    Auth login(LoginForm form);
    Long findByLogin(String login);
    void validate(Long id);
    void unValidate(Long id);
    List<UserEntity>  getAllUnvalidate();
    void sendPasswordMail(String login);
    void resetPassword(String password, String email);

}

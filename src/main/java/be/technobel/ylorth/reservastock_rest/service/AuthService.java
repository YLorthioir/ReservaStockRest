package be.technobel.ylorth.reservastock_rest.service;

import be.technobel.ylorth.reservastock_rest.model.dto.AuthDTO;
import be.technobel.ylorth.reservastock_rest.model.form.LoginForm;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import be.technobel.ylorth.reservastock_rest.model.form.StudentRegisterForm;

public interface AuthService {
    AuthDTO register(RegisterForm form);
    AuthDTO register(StudentRegisterForm form);
    boolean checkEmailNotTaken(String email);
    AuthDTO login(LoginForm form);

}

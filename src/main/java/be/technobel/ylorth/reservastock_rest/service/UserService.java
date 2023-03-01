package be.technobel.ylorth.reservastock_rest.service;

import be.technobel.ylorth.reservastock_rest.model.dto.UserDTO;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;

import java.util.List;

public interface UserService {
    void register(RegisterForm form);
    void update(RegisterForm form);
    UserDTO getOne(Long id);
    boolean checkEmailNotTaken(String email);
    List<UserDTO> getAll();

}

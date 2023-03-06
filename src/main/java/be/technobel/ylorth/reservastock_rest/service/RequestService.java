package be.technobel.ylorth.reservastock_rest.service;

import be.technobel.ylorth.reservastock_rest.model.dto.RequestDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Request;
import be.technobel.ylorth.reservastock_rest.model.entity.Room;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.RequestForm;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface RequestService {
    List<RequestDTO> getAllUnconfirmed();
    List<RequestDTO> getAllByUser(String username);
    RequestDTO getOne(Long id);
    void insert(RequestForm form, Authentication authentication);
    void update(RequestForm form, Long id, Authentication authentication);
    void confirm(ConfirmForm form, Long id);
    void delete(Long id, Authentication authentication);
}

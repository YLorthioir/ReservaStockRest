package be.technobel.ylorth.reservastock_rest.service;

import be.technobel.ylorth.reservastock_rest.model.dto.RequestDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.RoomDTO;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.RequestForm;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RequestService {
    List<RequestDTO> getAllUnconfirmed();
    List<RequestDTO> getAllByUser(String username);
    RequestDTO getOne(Long id);
    void insert(RequestForm form, Authentication authentication);
    void update(RequestForm form, Long id, Authentication authentication);
    void confirm(ConfirmForm form, Long id, String login);
    void delete(Long id, Authentication authentication);
    List<RoomDTO> freeCorrespondingRooms(Long id);
}

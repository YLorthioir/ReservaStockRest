package be.technobel.ylorth.reservastock_rest.bll.service;

import be.technobel.ylorth.reservastock_rest.dal.models.RequestEntity;
import be.technobel.ylorth.reservastock_rest.dal.models.RoomEntity;
import be.technobel.ylorth.reservastock_rest.pl.models.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.pl.models.RequestForm;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RequestService {
    List<RequestEntity> getAllUnconfirmed();
    List<RequestEntity> getAllByUser(String username);
    RequestEntity getOne(Long id);
    HttpStatus insert(RequestForm form, Authentication authentication);
    void update(RequestForm form, Long id, Authentication authentication);
    HttpStatus confirm(ConfirmForm form, Long id, String login);
    void delete(Long id, Authentication authentication);
    List<RoomEntity> freeCorrespondingRooms(Long id);
}

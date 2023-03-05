package be.technobel.ylorth.reservastock_rest.service;

import be.technobel.ylorth.reservastock_rest.model.dto.RoomDTO;
import be.technobel.ylorth.reservastock_rest.model.form.RoomForm;

import java.util.List;

public interface RoomService {
    List<RoomDTO> getAll();
    RoomDTO getOne(Long id);
    void insert(RoomForm form);
    void update(RoomForm form, Long id);
}

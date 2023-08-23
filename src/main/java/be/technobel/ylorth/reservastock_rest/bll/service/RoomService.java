package be.technobel.ylorth.reservastock_rest.bll.service;

import be.technobel.ylorth.reservastock_rest.dal.models.Room;
import be.technobel.ylorth.reservastock_rest.pl.models.RoomForm;

import java.util.List;

public interface RoomService {
    List<Room> getAll();
    Room getOne(Long id);
    void insert(RoomForm form);
    void update(RoomForm form, Long id);
}

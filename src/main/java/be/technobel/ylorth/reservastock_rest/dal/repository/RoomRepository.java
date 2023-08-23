package be.technobel.ylorth.reservastock_rest.dal.repository;


import be.technobel.ylorth.reservastock_rest.dal.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoomRepository extends JpaRepository<Room,Long> {

    Set<Room> findAllByCapacity(int capacity);
}

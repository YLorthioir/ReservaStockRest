package be.technobel.ylorth.reservastock_rest.dal.repository;


import be.technobel.ylorth.reservastock_rest.dal.models.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoomRepository extends JpaRepository<RoomEntity,Long> {

    Set<RoomEntity> findAllByCapacity(int capacity);
}

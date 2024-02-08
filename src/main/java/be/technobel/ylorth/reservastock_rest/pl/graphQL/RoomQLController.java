package be.technobel.ylorth.reservastock_rest.pl.graphQL;

import be.technobel.ylorth.reservastock_rest.dal.repository.RoomRepository;
import be.technobel.ylorth.reservastock_rest.pl.models.Room;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RoomQLController {
    private RoomRepository roomRepository;

    public RoomQLController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @QueryMapping
    public Room room(@Argument long id) {
        return Room.fromBLL(this.roomRepository.findById(id).orElseThrow(() -> new RuntimeException("")));
    }

    @QueryMapping
    public List<Room> rooms(){
        return roomRepository.findAll().stream().map(Room::fromBLL).toList();
    }

}

package be.technobel.ylorth.reservastock_rest.pl.rest;

import be.technobel.ylorth.reservastock_rest.pl.models.Room;
import be.technobel.ylorth.reservastock_rest.pl.models.RoomForm;
import be.technobel.ylorth.reservastock_rest.bll.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/all")
    public List<Room> getAll(){
        return roomService.getAll().stream().map(Room::fromBLL).toList();
    }

    @GetMapping("/{id:[0-9]+}")
    public Room getOne(@PathVariable long id){
        return Room.fromBLL(roomService.getOne(id));
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid RoomForm form){
        roomService.insert(form);
    }

    @PutMapping("/{id:[0-9]+}")
    public void updateForm(@PathVariable long id, @RequestBody @Valid RoomForm form){
        roomService.update(form,id);
    }
}

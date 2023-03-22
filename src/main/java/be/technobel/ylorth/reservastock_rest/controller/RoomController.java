package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.RoomDTO;
import be.technobel.ylorth.reservastock_rest.model.form.RoomForm;
import be.technobel.ylorth.reservastock_rest.repository.MaterialRepository;
import be.technobel.ylorth.reservastock_rest.service.MaterialService;
import be.technobel.ylorth.reservastock_rest.service.RoomService;
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
    public List<RoomDTO> getAll(){
        return roomService.getAll();
    }

    @GetMapping("/{id:[0-9]+}")
    public RoomDTO getOne(@PathVariable long id){
        return roomService.getOne(id);
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

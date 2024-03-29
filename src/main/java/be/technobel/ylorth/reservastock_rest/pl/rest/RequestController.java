package be.technobel.ylorth.reservastock_rest.pl.rest;

import be.technobel.ylorth.reservastock_rest.pl.models.Request;
import be.technobel.ylorth.reservastock_rest.pl.models.Room;
import be.technobel.ylorth.reservastock_rest.pl.models.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.pl.models.RequestForm;
import be.technobel.ylorth.reservastock_rest.bll.service.AuthService;
import be.technobel.ylorth.reservastock_rest.bll.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/request")
public class RequestController {
    private final RequestService requestService;
    private final AuthService authService;


    public RequestController(RequestService requestService, AuthService authService) {
        this.requestService = requestService;
        this.authService = authService;
    }

    @GetMapping("/allUnconfirmed")
    public List<Request> getAllUnconfirmed(){
        return requestService.getAllUnconfirmed().stream().map(Request::fromBLL).toList();
    }

    @GetMapping("/{id:[0-9]+}")
    public Request getOne(@PathVariable long id){
        return Request.fromBLL(requestService.getOne(id));
    }

    @GetMapping("/all")
    public List<Request> getAllByUser(Authentication authentication){
        return requestService.getAllByUser(authentication.getPrincipal().toString()).stream().map(Request::fromBLL).toList();
    }

    @PostMapping("/add")
    public HttpStatus addRequest(@RequestBody @Valid RequestForm form, Authentication authentication){
        return requestService.insert(form, authentication);
    }

    @PatchMapping("/{id:[0-9]+}/confirm")
    public HttpStatus processConfirmForm(@PathVariable Long id,@RequestBody @Valid ConfirmForm form, Authentication authentication){
        if(form.isValid()) {
            form.setRefusalReason(null);
        }
        String login = authentication.getPrincipal().toString();
        return requestService.confirm(form, id, login);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable long id, Authentication authentication){
        requestService.delete(id, authentication);
    }

    @GetMapping("/freeRooms/{id:[0-9]+}")
    public List<Room> getAllFreeRoom(@PathVariable Long id){
        return requestService.freeCorrespondingRooms(id).stream().map(Room::fromBLL).toList();
    }
}

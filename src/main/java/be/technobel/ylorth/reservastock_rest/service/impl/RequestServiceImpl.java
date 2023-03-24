package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.model.dto.RequestDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.RoomDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Request;
import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.model.entity.Room;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.RequestForm;
import be.technobel.ylorth.reservastock_rest.repository.RequestRepository;
import be.technobel.ylorth.reservastock_rest.repository.MaterialRepository;
import be.technobel.ylorth.reservastock_rest.repository.RoomRepository;
import be.technobel.ylorth.reservastock_rest.repository.UserRepository;
import be.technobel.ylorth.reservastock_rest.service.RequestService;
import be.technobel.ylorth.reservastock_rest.service.mapper.RequestMapper;
import be.technobel.ylorth.reservastock_rest.service.mapper.RoomMapper;
import be.technobel.ylorth.reservastock_rest.service.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final MaterialRepository materialRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoomMapper roomMapper;

    public RequestServiceImpl(RequestRepository requestRepository, RequestMapper requestMapper, MaterialRepository materialRepository, RoomRepository roomRepository,
                              UserRepository userRepository, UserMapper userMapper, RoomMapper roomMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.materialRepository = materialRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
    }

    @Override
    public List<RequestDTO> getAllUnconfirmed() {
        return requestRepository.getAllUnconfirmed().stream()
                .map(requestMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<RequestDTO> getAllByUser(String username){
        return requestRepository.findAll().stream()
                .map(requestMapper::toDTO)
                .filter(d -> d.getUserDTO().equals( userMapper.toUserDTO(userRepository.findByLogin(username).get())))
                .toList();
    }

    @Override
    public RequestDTO getOne(Long id) {
        return requestRepository.findById(id)
                .map( requestMapper::toDTO )
                .orElseThrow(() -> new NotFoundException("request not found"));
    }

    @Override
    public HttpStatus insert(RequestForm form, Authentication authentication) {
        if( form == null )
            throw new IllegalArgumentException("form should not be null");

        Request entity = requestMapper.requestToEntity(form);
        entity.setMaterials(
                new HashSet<>(materialRepository.findAllById(form.getMaterials()))
        );
        entity.setUser(userRepository.findByLogin(authentication.getPrincipal().toString()).get());
        entity.setRoom(roomRepository.findById(form.getRoom()).get());

        entity=verification(entity);
        requestRepository.save(entity);

        if(entity.getRefusalReason()==null)
            return HttpStatus.CREATED;
        else
            return HttpStatus.ACCEPTED;
    }

    @Override
    public HttpStatus confirm(ConfirmForm form, Long id, String login) {
        Request entity = requestRepository.findById(id).get();

        if(form.getRoom()==null)
            form.setRoom(entity.getRoom().getId());

        entity.setRoom(roomRepository.findById(form.getRoom()).get());
        entity = verification(entity);

        if(form.isValid() && entity.getRefusalReason()==null)
            entity.setAdmin(userRepository.findByLogin(login).get());
        else if(entity.getRefusalReason()==null)
            entity.setRefusalReason(form.getRefusalReason());

        entity.setRoom(roomRepository.findById(form.getRoom()).get());

        requestRepository.save(entity);

        if(entity.getRefusalReason()==null)
            return HttpStatus.CREATED;
        else
            return HttpStatus.ACCEPTED;
    }

    @Override
    public void delete(Long id, Authentication authentication) {
        if( !requestRepository.existsById(id) )
            throw new NotFoundException("Request not found");

        if(requestRepository.findById(id).get().getUser().getLogin().equals(authentication.getPrincipal().toString()) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            requestRepository.deleteById(id);
    }

    @Override
    public void update(RequestForm form, Long id, Authentication authentication) {
        if( form == null )
            throw new IllegalArgumentException("form should not be null");

        if( !requestRepository.findById(id).get().getUser().getLogin().equals(authentication.getPrincipal().toString()) )
            throw new IllegalArgumentException("not allowed to update this request");

        Request entity = requestMapper.requestToEntity(form);
        entity.setMaterials(
                new HashSet<>(materialRepository.findAllById(form.getMaterials()))
        );
        entity.setUser(userRepository.findByLogin(authentication.getPrincipal().toString()).get());
        entity.setRoom(roomRepository.findById(form.getRoom()).get());

        entity.setId(id);
        requestRepository.save(verification(entity));
    }
    private Request verification(Request entity){

        //List of concordant rooms

        Set<Room> concordantRooms = correspondingRooms(entity);

        //if request is validate (have an id), put this room
        if(entity.getId()>0) {
            concordantRooms.removeAll(concordantRooms);
            concordantRooms.add(entity.getRoom());
        }

        //if there is no concordant room, set refusal reason
        if(concordantRooms.size()==0)
            entity.setRefusalReason("No room found with the required material");


        //list of validated request with corresponding room on the request moment
        List<Request> validatedConcordantRoomRequest = new ArrayList<>();

        //set of occupied room
        Set<Room> occupiedRoom = new HashSet<>();

        concordantRooms.forEach(room ->  {

            requestRepository.findAll().stream()
                    .filter(request -> room == request.getRoom() && request.getAdmin()!= null && request.getRequestReason()==null)
                    .forEach(demande -> {
                        if(!(entity.getStartTime().plusMinutes(entity.getMinutes()).isBefore(demande.getStartTime()) || entity.getStartTime().isAfter(demande.getStartTime().plusMinutes(demande.getMinutes())))) {
                            occupiedRoom.add(room);
                            validatedConcordantRoomRequest.add(demande);
                        }
                    });
        });

        /*
        -> if occupiedRoom < concordantRoom: at least one room is free for the request
        -> if non, look if request made by professor
        -> if no match, set refusal reason
         */

        if(occupiedRoom.size()== concordantRooms.size() && (entity.getUser().getRoles().contains(Role.PROFESSOR) || entity.getUser().getRoles().contains(Role.ADMIN))){
            for (Room room : occupiedRoom) {

                //list of request who have the same room
                List<Request> requestValideeParSalle = validatedConcordantRoomRequest.stream()
                        .filter(demande -> demande.getRoom()== room)
                        .toList();

                //search of request made by professors
                List<Request> requestProf = requestValideeParSalle.stream()
                        .filter(demande -> demande.getUser().getRoles().contains(Role.PROFESSOR))
                        .toList();

                /*
                if in existing requests during the requested time slot,
                there are only those of students. we delete them to put that of the teacher
                */

                if(requestProf.size()==0) {
                    requestValideeParSalle.stream()
                            .forEach(d -> {
                                d.setRefusalReason("Request cancelled");
                                requestRepository.save(d);
                                entity.setRefusalReason(null);
                            });
                    return entity;
                }
            }
        } else if (occupiedRoom.size()== concordantRooms.size()) {
            entity.setRefusalReason("No room available found!");
        }
        return entity;
    }
    private Set<Room> correspondingRooms(Request entity){

        //Rooms with same capacity
        Set<Room> concordantRooms = roomRepository.findAllByCapacity(entity.getRoom().getCapacity());

        //removed rooms who haven't required material
        return concordantRooms.stream()
                .filter(r -> r.getContains().containsAll(entity.getMaterials()))
                .filter(r -> !(entity.getUser().getRoles().contains(Role.STUDENT)&& r.isForStaff()))
                .collect(Collectors.toSet());

    }
    public List<RoomDTO> freeCorrespondingRooms(Long id){

        Request entity = requestRepository.findById(id).get();
        //Rooms with same capacity

        Set<Room> correspondingRooms =correspondingRooms(entity);

        //If no room with needed material
        if(correspondingRooms.size()==0)
            entity.setRefusalReason("Pas de salles contenant le materiel nécessaire");

        List<Request> requestOfTheDay = requestRepository.findAll().stream()
                .filter(d -> d.getStartTime().getYear()==entity.getStartTime().getYear())
                .filter(d -> d.getStartTime().getDayOfYear()==entity.getStartTime().getDayOfYear())
                .filter(d -> d.getRoom().getCapacity()==entity.getRoom().getCapacity())
                .filter(d -> d.getAdmin()!=null)
                .toList();

        Set<RoomDTO> freeCorrespondingRooms = new HashSet<>();

        for (Room room : correspondingRooms) {
            for (Request acceptedRequest : requestOfTheDay) {
                if(acceptedRequest.getRoom().getId()!= room.getId() || (acceptedRequest.getStartTime().isAfter(entity.getStartTime().plusMinutes(entity.getMinutes())) || acceptedRequest.getStartTime().plusMinutes(acceptedRequest.getMinutes()).isBefore(entity.getStartTime()))){
                    freeCorrespondingRooms.add(roomMapper.toDTO(room));
                }
            }
        }

        return correspondingRooms.stream()
                .map( room -> roomMapper.toDTO(room))
                .collect(Collectors.toList());
    }
}

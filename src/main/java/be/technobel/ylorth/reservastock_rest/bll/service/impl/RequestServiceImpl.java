package be.technobel.ylorth.reservastock_rest.bll.service.impl;

import be.technobel.ylorth.reservastock_rest.bll.service.RequestService;
import be.technobel.ylorth.reservastock_rest.bll.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.dal.models.RequestEntity;
import be.technobel.ylorth.reservastock_rest.dal.models.Role;
import be.technobel.ylorth.reservastock_rest.dal.models.RoomEntity;
import be.technobel.ylorth.reservastock_rest.dal.models.UserEntity;
import be.technobel.ylorth.reservastock_rest.pl.models.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.pl.models.RequestForm;
import be.technobel.ylorth.reservastock_rest.dal.repository.RequestRepository;
import be.technobel.ylorth.reservastock_rest.dal.repository.MaterialRepository;
import be.technobel.ylorth.reservastock_rest.dal.repository.RoomRepository;
import be.technobel.ylorth.reservastock_rest.dal.repository.UserRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
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
    private final MaterialRepository materialRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RequestServiceImpl(RequestRepository requestRepository,
                              MaterialRepository materialRepository,
                              RoomRepository roomRepository,
                              UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.materialRepository = materialRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RequestEntity> getAllUnconfirmed() {

        Specification<RequestEntity> spec = (root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.isNull(root.get("admin")), criteriaBuilder.isNull(root.get("refusalReason")));

        return requestRepository.findAll(spec);

        //return requestRepository.getAllUnconfirmed();
    }
    @Override
    public List<RequestEntity> getAllByUser(String username){

        Specification<RequestEntity> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("userEntity").get("login"), username));

        return requestRepository.findAll(spec);

//        return requestRepository.findAll().stream()
//                .filter(d -> d.getUserEntity().equals( userRepository.findByLogin(username).get()))
//                .toList();
    }

    @Override
    public RequestEntity getOne(Long id) {

        Specification<RequestEntity> spec = (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),id)));

        return requestRepository.findOne(spec).orElseThrow(() -> new NotFoundException("request not found"));

//        return requestRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("request not found"));
    }

    @Override
    public HttpStatus insert(RequestForm form, Authentication authentication) {
        if( form == null )
            throw new IllegalArgumentException("form should not be null");

        RequestEntity entity = new RequestEntity();

        entity.setMinutes(form.getMinutes());
        entity.setRequestReason(form.getRequestReason());
        entity.setStartTime(form.getStartTime());

        entity.setMaterialEntities(
                new HashSet<>(materialRepository.findAllById(form.getMaterials()))
        );
        entity.setUserEntity(userRepository.findByLogin(authentication.getPrincipal().toString()).get());
        entity.setRoomEntity(roomRepository.findById(form.getRoom()).get());

        entity=verification(entity);
        requestRepository.save(entity);

        if(entity.getRefusalReason()==null)
            return HttpStatus.CREATED;
        else
            return HttpStatus.ACCEPTED;
    }

    @Override
    public HttpStatus confirm(ConfirmForm form, Long id, String login) {
        RequestEntity entity = requestRepository.findById(id).get();

        if(form.getRoom()==null)
            form.setRoom(entity.getRoomEntity().getId());

        entity.setRoomEntity(roomRepository.findById(form.getRoom()).get());
        entity = verification(entity);

        if(form.isValid() && entity.getRefusalReason()==null)
            entity.setAdmin(userRepository.findByLogin(login).get());
        else if(entity.getRefusalReason()==null)
            entity.setRefusalReason(form.getRefusalReason());

        entity.setRoomEntity(roomRepository.findById(form.getRoom()).get());

        requestRepository.save(entity);

        if(entity.getRefusalReason()==null)
            return HttpStatus.CREATED;
        else
            return HttpStatus.ACCEPTED;
    }

    @Override
    public void delete(Long id, Authentication authentication) {
        if( !requestRepository.existsById(id) )
            throw new NotFoundException("RequestEntity not found");

        if(requestRepository.findById(id).get().getUserEntity().getLogin().equals(authentication.getPrincipal().toString()) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            requestRepository.deleteById(id);
    }

    @Override
    public void update(RequestForm form, Long id, Authentication authentication) {
        if( form == null )
            throw new IllegalArgumentException("form should not be null");

        if( !requestRepository.findById(id).get().getUserEntity().getLogin().equals(authentication.getPrincipal().toString()) )
            throw new IllegalArgumentException("not allowed to update this request");
        RequestEntity entity = new RequestEntity();

        entity.setMinutes(form.getMinutes());
        entity.setRequestReason(form.getRequestReason());
        entity.setStartTime(form.getStartTime());

        entity.setMaterialEntities(
                new HashSet<>(materialRepository.findAllById(form.getMaterials()))
        );
        entity.setUserEntity(userRepository.findByLogin(authentication.getPrincipal().toString()).get());
        entity.setRoomEntity(roomRepository.findById(form.getRoom()).get());

        entity.setId(id);
        requestRepository.save(verification(entity));
    }

    private RequestEntity verification(RequestEntity entity){

        //List of concordant rooms

        Set<RoomEntity> concordantRoomEntities = correspondingRooms(entity);

        //if request is validate (have an id), put this room
        if(entity.getId()>0) {
            concordantRoomEntities.removeAll(concordantRoomEntities);
            concordantRoomEntities.add(entity.getRoomEntity());
        }

        //if there is no concordant room, set refusal reason
        if(concordantRoomEntities.size()==0)
            entity.setRefusalReason("No room found with the required material");


        //list of validated request with corresponding room on the request moment
        List<RequestEntity> validatedConcordantRoomRequestEntity = new ArrayList<>();

        //set of occupied room
        Set<RoomEntity> occupiedRoomEntity = new HashSet<>();

        concordantRoomEntities.forEach(room ->  {

            requestRepository.findAll().stream()
                    .filter(request -> room == request.getRoomEntity() && request.getAdmin()!= null && request.getRequestReason()==null)
                    .forEach(demande -> {
                        if(!(entity.getStartTime().plusMinutes(entity.getMinutes()).isBefore(demande.getStartTime()) || entity.getStartTime().isAfter(demande.getStartTime().plusMinutes(demande.getMinutes())))) {
                            occupiedRoomEntity.add(room);
                            validatedConcordantRoomRequestEntity.add(demande);
                        }
                    });
        });

        /*
        -> if occupiedRoomEntity < concordantRoom: at least one room is free for the request
        -> if non, look if request made by professor
        -> if no match, set refusal reason
         */

        if(occupiedRoomEntity.size()== concordantRoomEntities.size() && (entity.getUserEntity().getRoles().contains(Role.PROFESSOR) || entity.getUserEntity().getRoles().contains(Role.ADMIN))){
            for (RoomEntity roomEntity : occupiedRoomEntity) {

                //list of request who have the same roomEntity
                List<RequestEntity> requestEntityValideeParSalle = validatedConcordantRoomRequestEntity.stream()
                        .filter(demande -> demande.getRoomEntity()== roomEntity)
                        .toList();

                //search of request made by professors
                List<RequestEntity> requestEntityProf = requestEntityValideeParSalle.stream()
                        .filter(demande -> demande.getUserEntity().getRoles().contains(Role.PROFESSOR))
                        .toList();

                /*
                if in existing requests during the requested time slot,
                there are only those of students. we delete them to put that of the teacher
                */

                if(requestEntityProf.size()==0) {
                    requestEntityValideeParSalle.stream()
                            .forEach(d -> {
                                d.setRefusalReason("RequestEntity cancelled");
                                requestRepository.save(d);
                                entity.setRefusalReason(null);
                            });
                    return entity;
                }
            }
        } else if (occupiedRoomEntity.size()== concordantRoomEntities.size()) {
            entity.setRefusalReason("No room available found!");
        }
        return entity;
    }

    private Set<RoomEntity> correspondingRooms(RequestEntity entity){

        //Rooms with same capacity
        Set<RoomEntity> concordantRoomEntities = roomRepository.findAllByCapacity(entity.getRoomEntity().getCapacity());

        //removed rooms who haven't required material
        return concordantRoomEntities.stream()
                .filter(r -> r.getContains().containsAll(entity.getMaterialEntities()))
                .filter(r -> !(entity.getUserEntity().getRoles().contains(Role.STUDENT)&& r.isForStaff()))
                .collect(Collectors.toSet());

    }

    public List<RoomEntity> freeCorrespondingRooms(Long id){

        RequestEntity entity = requestRepository.findById(id).get();
        //Rooms with same capacity

        Set<RoomEntity> correspondingRoomEntities =correspondingRooms(entity);

        //If no room with needed material
        if(correspondingRoomEntities.size()==0)
            entity.setRefusalReason("Pas de salles contenant le materiel nécessaire");

        List<RequestEntity> requestEntityOfTheDay = requestRepository.findAll().stream()
                .filter(d -> d.getStartTime().getYear()==entity.getStartTime().getYear())
                .filter(d -> d.getStartTime().getDayOfYear()==entity.getStartTime().getDayOfYear())
                .filter(d -> d.getRoomEntity().getCapacity()==entity.getRoomEntity().getCapacity())
                .filter(d -> d.getAdmin()!=null)
                .toList();

        Set<RoomEntity> freeCorrespondingRoomEntities = new HashSet<>();

        for (RoomEntity roomEntity : correspondingRoomEntities) {
            for (RequestEntity acceptedRequestEntity : requestEntityOfTheDay) {
                if(acceptedRequestEntity.getRoomEntity().getId()!= roomEntity.getId() || (acceptedRequestEntity.getStartTime().isAfter(entity.getStartTime().plusMinutes(entity.getMinutes())) || acceptedRequestEntity.getStartTime().plusMinutes(acceptedRequestEntity.getMinutes()).isBefore(entity.getStartTime()))){
                    freeCorrespondingRoomEntities.add(roomEntity);
                }
            }
        }

        return correspondingRoomEntities.stream()
                .collect(Collectors.toList());
    }
}

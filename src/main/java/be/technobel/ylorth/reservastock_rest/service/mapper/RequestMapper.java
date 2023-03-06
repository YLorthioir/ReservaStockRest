package be.technobel.ylorth.reservastock_rest.service.mapper;

import be.technobel.ylorth.reservastock_rest.model.dto.RequestDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Request;
import be.technobel.ylorth.reservastock_rest.model.form.RequestForm;
import be.technobel.ylorth.reservastock_rest.repository.MaterialRepository;
import be.technobel.ylorth.reservastock_rest.repository.RoomRepository;
import be.technobel.ylorth.reservastock_rest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RequestMapper {

    private MaterialMapper materialMapper;
    private final MaterialRepository materialRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RequestMapper(MaterialMapper materialMapper, MaterialRepository materialRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.materialMapper = materialMapper;
        this.materialRepository = materialRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public RequestDTO toDTO(Request entity){

        if(entity == null)
            return null;

        Long adminId=null;
        if(entity.getAdmin()!=null)
            adminId=entity.getAdmin().getId();

        return RequestDTO.builder()
                .id(entity.getId())
                .startTime(entity.getStartTime())
                .minutes(entity.getMinutes())
                .roomId(entity.getRoom().getId())
                .adminId(adminId)
                .userId(entity.getUser().getId())
                .requestReason(entity.getRequestReason())
                .refusalReason(entity.getRefusalReason())
                .materials(
                        entity.getMaterials().stream()
                        .map(materialMapper::toDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    public Request requestToEntity(RequestForm form){

        if(form == null)
            return null;

        Request request = new Request();

        request.setStartTime(form.getStarTime());
        request.setRequestReason(form.getRequestReason());
        request.setMinutes(form.getMinutes());
        request.setMaterials(form.getMaterials().stream()
                .map(l -> materialRepository.findById(l).get())
                .collect(Collectors.toSet()));

        return request;
    }


    public Request toEntity(RequestDTO requestDTO){

        if(requestDTO == null)
            return null;

        Request entity = new Request();

        entity.setId(requestDTO.getId());
        entity.setStartTime(requestDTO.getStartTime());
        entity.setMinutes(requestDTO.getMinutes());
        entity.setRoom(roomRepository.findById(requestDTO.getRoomId()).get());
        entity.setAdmin(userRepository.findById(requestDTO.getAdminId()).get());
        entity.setUser(userRepository.findById(requestDTO.getUserId()).get());
        entity.setRequestReason(requestDTO.getRequestReason());
        entity.setRefusalReason(requestDTO.getRefusalReason());
        entity.setMaterials(requestDTO.getMaterials().stream()
                .map(dto -> materialMapper.toEntity(dto))
                .collect(Collectors.toSet())
        );

        return entity;
    }
}

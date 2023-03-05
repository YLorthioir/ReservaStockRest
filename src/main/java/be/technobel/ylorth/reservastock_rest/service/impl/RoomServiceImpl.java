package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.model.dto.RoomDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Room;
import be.technobel.ylorth.reservastock_rest.model.form.RoomForm;
import be.technobel.ylorth.reservastock_rest.repository.MaterialRepository;
import be.technobel.ylorth.reservastock_rest.repository.RoomRepository;
import be.technobel.ylorth.reservastock_rest.service.RoomService;
import be.technobel.ylorth.reservastock_rest.service.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final MaterialRepository materialRepository;

    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper, MaterialRepository materialRepository) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.materialRepository = materialRepository;
    }

    @Override
    public List<RoomDTO> getAll() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toDTO)
                .toList();
    }

    @Override
    public RoomDTO getOne(Long id) {
        return roomRepository.findById(id)
                .map( roomMapper::toDTO )
                .orElseThrow(() -> new NotFoundException("Room not found"));
    }

    @Override
    public void insert(RoomForm form) {
        if( form == null )
            throw new IllegalArgumentException("form should not be null");

        Room entity = new Room();
        entity = roomMapper.toEntity(form);
        entity.setContains(
                new HashSet<>(materialRepository.findAllById(form.getContains()))
        );
        roomRepository.save(entity);
    }

    @Override
    public void update(RoomForm form, Long id) {
        Room entity = roomMapper.toEntity(form);
        entity.setId(id);
        roomRepository.save(entity);
    }
}

package be.technobel.ylorth.reservastock_rest.bll.service.impl;

import be.technobel.ylorth.reservastock_rest.bll.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.dal.models.RoomEntity;
import be.technobel.ylorth.reservastock_rest.pl.models.RoomForm;
import be.technobel.ylorth.reservastock_rest.dal.repository.MaterialRepository;
import be.technobel.ylorth.reservastock_rest.dal.repository.RoomRepository;
import be.technobel.ylorth.reservastock_rest.bll.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final MaterialRepository materialRepository;

    public RoomServiceImpl(RoomRepository roomRepository, MaterialRepository materialRepository) {
        this.roomRepository = roomRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    public List<RoomEntity> getAll() {
        return roomRepository.findAll().stream()
                .toList();
    }

    @Override
    public RoomEntity getOne(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RoomEntity not found"));
    }

    @Override
    public void insert(RoomForm form) {
        if( form == null )
            throw new IllegalArgumentException("form should not be null");

        RoomEntity entity = new RoomEntity();

        entity.setName(form.getName());
        entity.setCapacity(form.getCapacity());
        entity.setContains(
                new HashSet<>(materialRepository.findAllById(form.getContains()))
        );
        roomRepository.save(entity);
    }

    @Override
    public void update(RoomForm form, Long id) {

        RoomEntity entity = new RoomEntity();

        entity.setName(form.getName());
        entity.setCapacity(form.getCapacity());
        entity.setForStaff(form.isForStaff());
        entity.setContains(
                new HashSet<>(materialRepository.findAllById(form.getContains()))
        );
        entity.setId(id);
        roomRepository.save(entity);
    }
}

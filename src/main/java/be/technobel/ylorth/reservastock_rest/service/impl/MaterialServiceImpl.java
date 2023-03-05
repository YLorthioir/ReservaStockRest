package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.model.dto.MaterialDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Material;
import be.technobel.ylorth.reservastock_rest.repository.MaterialRepository;
import be.technobel.ylorth.reservastock_rest.service.MaterialService;
import be.technobel.ylorth.reservastock_rest.service.mapper.MaterialMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    public MaterialServiceImpl(MaterialRepository materialRepository, MaterialMapper materialMapper) {
        this.materialRepository = materialRepository;
        this.materialMapper = materialMapper;
    }

    @Override
    public List<MaterialDTO> getAll() {
        return materialRepository.findAll().stream()
                .map(materialMapper::toDTO)
                .toList();
    }

    @Override
    public MaterialDTO getOne(Long id) {
        return materialRepository.findById(id)
                .map( materialMapper::toDTO )
                .orElseThrow(() -> new NotFoundException("material not found"));
    }

    @Override
    public void insert(String name) {
        Material entity = new Material();
        entity.setName(name);
        materialRepository.save(entity);
    }

    @Override
    public void delete(long id) {
        if( !materialRepository.existsById(id) )
            throw new NotFoundException("Material not found");

        materialRepository.deleteById(id);
    }
}

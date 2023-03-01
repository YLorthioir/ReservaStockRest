package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.model.dto.SalleDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Salle;
import be.technobel.ylorth.reservastock_rest.model.form.SalleForm;
import be.technobel.ylorth.reservastock_rest.repository.MaterielRepository;
import be.technobel.ylorth.reservastock_rest.repository.SalleRepository;
import be.technobel.ylorth.reservastock_rest.service.SalleService;
import be.technobel.ylorth.reservastock_rest.service.mapper.SalleMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;
    private final SalleMapper salleMapper;
    private final MaterielRepository materielRepository;

    public SalleServiceImpl(SalleRepository salleRepository, SalleMapper salleMapper, MaterielRepository materielRepository) {
        this.salleRepository = salleRepository;
        this.salleMapper = salleMapper;
        this.materielRepository = materielRepository;
    }

    @Override
    public List<SalleDTO> getAll() {
        return salleRepository.findAll().stream()
                .map(salleMapper::toDTO)
                .toList();
    }

    @Override
    public SalleDTO getOne(Long id) {
        return salleRepository.findById(id)
                .map( salleMapper::toDTO )
                .orElseThrow(() -> new NotFoundException("Salle not found"));
    }

    @Override
    public void insert(SalleForm form) {
        if( form == null )
            throw new IllegalArgumentException("form should not be null");

        Salle entity = new Salle();
        entity = salleMapper.toEntity(form);
        entity.setContient(
                new HashSet<>(materielRepository.findAllById(form.getContient()))
        );
        salleRepository.save(entity);
    }

    @Override
    public void update(SalleForm form, Long id) {
        Salle entity = salleMapper.toEntity(form);
        entity.setId(id);
        salleRepository.save(entity);
    }
}

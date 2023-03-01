package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.model.dto.MaterielDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Materiel;
import be.technobel.ylorth.reservastock_rest.repository.MaterielRepository;
import be.technobel.ylorth.reservastock_rest.service.MaterielService;
import be.technobel.ylorth.reservastock_rest.service.mapper.MaterielMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterielServiceImpl implements MaterielService {

    private final MaterielRepository materielRepository;
    private final MaterielMapper materielMapper;

    public MaterielServiceImpl(MaterielRepository materielRepository, MaterielMapper materielMapper) {
        this.materielRepository = materielRepository;
        this.materielMapper = materielMapper;
    }

    @Override
    public List<MaterielDTO> getAll() {
        return materielRepository.findAll().stream()
                .map(materielMapper::toDTO)
                .toList();
    }

    @Override
    public MaterielDTO getOne(Long id) {
        return materielRepository.findById(id)
                .map( materielMapper::toDTO )
                .orElseThrow(() -> new NotFoundException("materiel not found"));
    }

    @Override
    public void insert(String nom) {
        Materiel entity = new Materiel();
        entity.setNom(nom);
        materielRepository.save(entity);
    }

    @Override
    public void delete(long id) {
        if( !materielRepository.existsById(id) )
            throw new NotFoundException("Materiel not found");

        materielRepository.deleteById(id);
    }
}

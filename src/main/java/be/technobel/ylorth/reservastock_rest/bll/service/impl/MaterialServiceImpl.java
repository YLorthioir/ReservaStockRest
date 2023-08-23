package be.technobel.ylorth.reservastock_rest.bll.service.impl;

import be.technobel.ylorth.reservastock_rest.bll.service.MaterialService;
import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.pl.models.MaterialDTO;
import be.technobel.ylorth.reservastock_rest.dal.models.Material;
import be.technobel.ylorth.reservastock_rest.dal.repository.MaterialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    public List<Material> getAll() {
        return materialRepository.findAll().stream()
                .toList();
    }

    @Override
    public Material getOne(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("material not found"));
    }

    @Override
    public void insert(String name) {
        Material entity = new Material();
        entity.setName(name);
        materialRepository.save(entity);
    }

    @Override
    public HttpStatus delete(long id) {
        if( !materialRepository.existsById(id) )
            throw new NotFoundException("Material not found");
        try{
            materialRepository.deleteById(id);
            return HttpStatus.CREATED;
        } catch(Exception e){
            if(e.getClass().toString().equals("class org.springframework.dao.DataIntegrityViolationException"))
                return HttpStatus.BAD_REQUEST;
            else
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}

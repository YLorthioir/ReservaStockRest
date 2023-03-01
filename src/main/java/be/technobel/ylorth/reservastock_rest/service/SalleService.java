package be.technobel.ylorth.reservastock_rest.service;

import be.technobel.ylorth.reservastock_rest.model.dto.SalleDTO;
import be.technobel.ylorth.reservastock_rest.model.form.SalleForm;

import java.util.List;

public interface SalleService {
    List<SalleDTO> getAll();
    SalleDTO getOne(Long id);
    void insert(SalleForm form);
    void update(SalleForm form, Long id);
}

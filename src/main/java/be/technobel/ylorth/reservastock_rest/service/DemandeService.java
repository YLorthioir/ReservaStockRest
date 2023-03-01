package be.technobel.ylorth.reservastock_rest.service;

import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.SalleDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Demande;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;

import java.util.List;

public interface DemandeService {
    List<DemandeDTO> getAllUnconfirm();
    List<DemandeDTO> getAllByUser(Long id);
    DemandeDTO getOne(Long id);
    void insert(DemandeForm form);
    void update(DemandeForm form, Long id);
    void confirm(ConfirmForm form, Long id);
    void delete(Long id);
    Demande verification(Demande entity);
    List<SalleDTO> listeSalleDispo(DemandeDTO demande);
}

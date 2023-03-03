package be.technobel.ylorth.reservastock_rest.service;

import be.technobel.ylorth.reservastock_rest.model.dto.AuthDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.SalleDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Demande;
import be.technobel.ylorth.reservastock_rest.model.entity.Salle;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface DemandeService {
    List<DemandeDTO> getAllUnconfirm();
    List<DemandeDTO> getAllByUser(String username);
    DemandeDTO getOne(Long id);
    void insert(DemandeForm form, Authentication authentication);
    void update(DemandeForm form, Long id);
    void confirm(ConfirmForm form, Long id);
    void delete(Long id, Authentication authentication);
    Demande verification(Demande entity);
    Set<Salle> sallesCorrespondante(Demande entity);
}

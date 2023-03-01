package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import be.technobel.ylorth.reservastock_rest.model.dto.MaterielDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.model.form.ConfirmForm;
import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;
import be.technobel.ylorth.reservastock_rest.repository.DemandeRepository;
import be.technobel.ylorth.reservastock_rest.service.AuthService;
import be.technobel.ylorth.reservastock_rest.service.DemandeService;
import be.technobel.ylorth.reservastock_rest.service.MaterielService;
import be.technobel.ylorth.reservastock_rest.service.SalleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/demande")
public class DemandeController {
    private final DemandeService demandeService;
    private final SalleService salleService;
    private final MaterielService materielService;
    private final AuthService userService;
    private final DemandeRepository demandeRepository;


    public DemandeController(DemandeService demandeService, SalleService salleService,
                             MaterielService materielService, AuthService userService,
                             DemandeRepository demandeRepository) {
        this.demandeService = demandeService;
        this.salleService = salleService;
        this.materielService = materielService;
        this.userService = userService;
        this.demandeRepository = demandeRepository;
    }

    //TODO
    @GetMapping("/all")
    public String getAllUnconfirm(Model model){
        model.addAttribute("listAdmin", demandeService.getAllUnconfirm().stream().filter(demandeDTO -> demandeDTO.getUser().getRole()== Role.ADMIN));
        model.addAttribute("listProf", demandeService.getAllUnconfirm().stream().filter(demandeDTO -> demandeDTO.getUser().getRole()== Role.PROFESSEUR));
        model.addAttribute("listEtu", demandeService.getAllUnconfirm().stream().filter(demandeDTO -> demandeDTO.getUser().getRole()== Role.ETUDIANT));
        return "demande/display-allunconfirm";
    }

    @GetMapping("/all/{id:[0-9]+}")
    public String getAllByUser(Model model,@PathVariable long id){
        model.addAttribute("list", demandeService.getAllByUser(id));
        model.addAttribute("salle",salleService.getAll());
        return "demande/display-allbyuser";
    }

    @GetMapping("/add")
    public String insertForm(Model model){

        model.addAttribute("form", new DemandeForm());
        model.addAttribute("materiels", materielService.getAll());
        model.addAttribute("listeSalle",salleService.getAll());
        return "demande/insert-form";
    }

    @PostMapping("/add")
    public String processInsertForm(@ModelAttribute("form") @Valid DemandeForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors() ) {
            return "demande/insert-form";
        }

        demandeService.insert(form);
        return "redirect:/demande/all/1";
    }

    @GetMapping("/{id:[0-9]+}")
    public String getOne(Model model, @PathVariable long id){
        model.addAttribute( "demande", demandeService.getOne(id) );
        return "demande/display-one";
    }

    @GetMapping("/{id:[0-9]+}/update")
    public String updateForm(Model model, @PathVariable long id){
        DemandeForm form = new DemandeForm();

        DemandeDTO demande = demandeService.getOne(id);
        form.setCreneau( demande.getCreneau() );
        form.setMinutes( demande.getMinutes() );
        form.setRaisonDemande( demande.getRaisonDemande());
        form.setUser(demande.getUser().getId());
        form.setSalle(demande.getSalle().getId());
        form.setMateriels(
                demande.getMateriels().stream()
                        .map(MaterielDTO::getId)
                        .collect(Collectors.toSet())
        );


        model.addAttribute("form", form);
        model.addAttribute("id", id);
        model.addAttribute("materiels", materielService.getAll());
        model.addAttribute("listeSalle",salleService.getAll());

        return "demande/update-form";
    }

    @PostMapping("/{id:[0-9]+}/update")
    public String processUpdateForm(
            @PathVariable Long id,
            @ModelAttribute("form") @Valid DemandeForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "demande/insert-form";
        }
        demandeService.update(form, id);
        return "redirect:/demande/all/1";
    }

    @GetMapping("/{id:[0-9]+}/confirm")
    public String confirmForm(Model model, @PathVariable long id){
        ConfirmForm form = new ConfirmForm();

        DemandeDTO demande = demandeService.getOne(id);
        form.setCreneau( demande.getCreneau() );
        form.setMinutes( demande.getMinutes() );
        form.setRaisonDemande( demande.getRaisonDemande());
        form.setUser(demande.getUser().getId());
        form.setSalle(demande.getSalle().getId());
        form.setMateriels(
                demande.getMateriels().stream()
                        .map(MaterielDTO::getId)
                        .collect(Collectors.toSet())
        );


        model.addAttribute("form", form);
        model.addAttribute("id", id);
        model.addAttribute("listeMateriel", materielService.getAll());
        model.addAttribute("listeSalle",salleService.getAll());
        model.addAttribute("listeSalleDispo", demandeService.listeSalleDispo(demande));

        return "demande/confirm-form";
    }

    @PostMapping("/{id:[0-9]+}/confirm")
    public String processConfirmForm(
            @PathVariable Long id,
            @ModelAttribute("form") @Valid ConfirmForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "demande/confirm-form";
        }
        form.setUser(1L);
        form.setAdmin(3L);
        demandeService.confirm(form, id);
        return "redirect:/demande/all";
    }
}

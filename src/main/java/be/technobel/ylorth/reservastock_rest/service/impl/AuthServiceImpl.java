package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.EmailAlreadyTakenException;
import be.technobel.ylorth.reservastock_rest.model.dto.UserDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.Adresse;
import be.technobel.ylorth.reservastock_rest.model.entity.ResetValidator;
import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.model.entity.User;
import be.technobel.ylorth.reservastock_rest.model.form.LoginForm;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import be.technobel.ylorth.reservastock_rest.repository.AdresseRepository;
import be.technobel.ylorth.reservastock_rest.repository.ResetValidatorRepository;
import be.technobel.ylorth.reservastock_rest.repository.UserRepository;
import be.technobel.ylorth.reservastock_rest.service.AuthService;
import be.technobel.ylorth.reservastock_rest.service.mapper.AdresseMapper;
import be.technobel.ylorth.reservastock_rest.service.mapper.UserMapper;
import be.technobel.ylorth.reservastock_rest.utils.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import be.technobel.ylorth.reservastock_rest.model.dto.AuthDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final EmailServiceImpl emailService;
    private final AdresseMapper adresseMapper;
    private final AdresseRepository adresseRepository;
    private final ResetValidatorRepository resetValidatorRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtProvider jwtProvider,
                           EmailServiceImpl emailService,
                           AdresseMapper adresseMapper,
                           AdresseRepository adresseRepository, ResetValidatorRepository resetValidatorRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.emailService = emailService;
        this.adresseMapper = adresseMapper;
        this.adresseRepository = adresseRepository;
        this.resetValidatorRepository = resetValidatorRepository;
    }

    @Override
    public void register(RegisterForm form) {
        if( userRepository.existsByEmail(form.getEmail()))
            throw new EmailAlreadyTakenException();

        Adresse adresse = adresseMapper.toEntity(form);

        adresse = adresseRepository.save(adresse);

        User user = userMapper.toEntity(form, adresse);

        String login = form.getNom().substring(0,3).concat(form.getPrenom().substring(0,3)).concat(String.valueOf(form.getDateDeNaissance().getDayOfYear()));

        user.setActif(true);
        user.setLogin(login);
        user.setRoles(form.getRoles());
        user.setMotDePasse( passwordEncoder.encode(form.getMotDePasse()) );
        user = userRepository.save( user );
    }

    @Override
    public void registerStudent(RegisterForm form) {
        if( userRepository.existsByEmail(form.getEmail()))
            throw new EmailAlreadyTakenException();

        Adresse adresse = adresseMapper.toEntity(form);

        adresse = adresseRepository.save(adresse);

        User user = userMapper.toEntity(form, adresse);

        String login = form.getNom().substring(0,3).concat(form.getPrenom().substring(0,3)).concat(String.valueOf(form.getDateDeNaissance().getDayOfYear()));

        user.setLogin(login);
        user.setMotDePasse( passwordEncoder.encode(form.getMotDePasse()) );
        user.getRoles().add(Role.ETUDIANT);
        user = userRepository.save( user );

    }

    @Override
    public boolean checkEmailNotTaken(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public AuthDTO login(LoginForm form) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(form.getLogin(),form.getMotDePasse()) );

        User user = userRepository.findByLogin(form.getLogin() )
                .orElseThrow();

        String token = jwtProvider.generateToken(user.getUsername(), List.copyOf(user.getRoles()) );

        return AuthDTO.builder()
                .token(token)
                .login(user.getLogin())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public Long findByLogin(String login) {
        return userRepository.findByLogin(login).get().getId();
    }

    @Override
    public void validate(Long id) {
        User user = userRepository.findById(id).get();
        user.setActif(true);
        userRepository.save(user);
    }

    @Override
    public void unValidate(Long id) {
        User user = userRepository.findById(id).get();
        user.setActif(false);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllUnvalidate() {
        return userRepository.getAllUnvalidate().stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void sendPasswordMail(String login) {
        User user = userRepository.findByLogin(login).get();

        if(resetValidatorRepository.existsByLogin(userRepository.findByLogin(login).get().getLogin()))
            resetValidatorRepository.deleteById(resetValidatorRepository.findByLogin(login).getId());

        String texte = " Bonjour,\n" +
                "Veuillez vous rendre ici pour changer de mot de passe: http://localhost:8080/swagger \n" +
                "Si vous n'avez pas fait cette demande, veuillez ignorer ce mail";

        emailService.sendMessage(user.getEmail(), "reset mot de passe", texte);

        ResetValidator resetValidator = new ResetValidator();
        resetValidator.setResetTime(LocalDateTime.now());
        resetValidator.setLogin(login);

        resetValidatorRepository.save(resetValidator);
    }

    @Override
    public void resetPassword(String password, String email) {

        if(resetValidatorRepository.existsByLogin(userRepository.findByEmail(email).get().getLogin()) && (LocalDateTime.now().isBefore(resetValidatorRepository.findByLogin(userRepository.findByEmail(email).get().getLogin()).getResetTime().plusMinutes(10)))){
            User user = userRepository.findByEmail(email).get();
            user.setMotDePasse(passwordEncoder.encode(password));
            userRepository.save(user);
            resetValidatorRepository.deleteById(resetValidatorRepository.findByLogin(userRepository.findByEmail(email).get().getLogin()).getId());

        }else if(resetValidatorRepository.existsByLogin(userRepository.findByEmail(email).get().getLogin()))
            resetValidatorRepository.deleteById(resetValidatorRepository.findByLogin(userRepository.findByEmail(email).get().getLogin()).getId());

    }

}

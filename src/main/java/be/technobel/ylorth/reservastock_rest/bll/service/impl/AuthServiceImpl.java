package be.technobel.ylorth.reservastock_rest.bll.service.impl;

import be.technobel.ylorth.reservastock_rest.bll.exception.EmailAlreadyTakenException;
import be.technobel.ylorth.reservastock_rest.dal.models.AdressEntity;
import be.technobel.ylorth.reservastock_rest.dal.models.ResetValidatorEntity;
import be.technobel.ylorth.reservastock_rest.dal.models.Role;
import be.technobel.ylorth.reservastock_rest.dal.models.UserEntity;
import be.technobel.ylorth.reservastock_rest.pl.models.LoginForm;
import be.technobel.ylorth.reservastock_rest.pl.models.RegisterForm;
import be.technobel.ylorth.reservastock_rest.dal.repository.AdressRepository;
import be.technobel.ylorth.reservastock_rest.dal.repository.ResetValidatorRepository;
import be.technobel.ylorth.reservastock_rest.dal.repository.UserRepository;
import be.technobel.ylorth.reservastock_rest.bll.service.AuthService;
import be.technobel.ylorth.reservastock_rest.pl.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import be.technobel.ylorth.reservastock_rest.pl.models.Auth;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final EmailServiceImpl emailService;
    private final AdressRepository adressRepository;
    private final ResetValidatorRepository resetValidatorRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtProvider jwtProvider,
                           EmailServiceImpl emailService,
                           AdressRepository adressRepository, ResetValidatorRepository resetValidatorRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.emailService = emailService;
        this.adressRepository = adressRepository;
        this.resetValidatorRepository = resetValidatorRepository;
    }

    @Override
    public void register(RegisterForm form) {
        if( userRepository.existsByEmail(form.getEmail()))
            throw new EmailAlreadyTakenException();

        AdressEntity adressEntity = new AdressEntity();

        adressEntity.setNumber(form.getNumber());
        adressEntity.setStreet(form.getStreet());
        adressEntity.setCountry(form.getCountry());
        adressEntity.setPostCode(form.getPostCode());
        adressEntity.setCity(form.getCity());

        adressEntity = adressRepository.save(adressEntity);

        UserEntity userEntity = new UserEntity();

        userEntity.setAdressEntity(adressEntity);
        userEntity.setEmail(form.getEmail());
        userEntity.setFirstName(form.getFirstName());
        userEntity.setLastName(form.getLastName());
        userEntity.setBirthdate(form.getBirthdate());
        userEntity.setPhone(form.getPhone());

        String login = form.getLastName().substring(0,3).concat(form.getFirstName().substring(0,3)).concat(String.valueOf(form.getBirthdate().getDayOfYear()));

        userEntity.setEnabled(true);
        userEntity.setLogin(login);
        userEntity.setRoles(form.getRoles());
        userEntity.setPassword( passwordEncoder.encode(form.getPassword()) );
        userEntity = userRepository.save(userEntity);
    }

    @Override
    public void registerStudent(RegisterForm form) {
        if( userRepository.existsByEmail(form.getEmail()))
            throw new EmailAlreadyTakenException();

        AdressEntity adressEntity = new AdressEntity();

        adressEntity.setNumber(form.getNumber());
        adressEntity.setStreet(form.getStreet());
        adressEntity.setCountry(form.getCountry());
        adressEntity.setPostCode(form.getPostCode());
        adressEntity.setCity(form.getCity());

        adressEntity = adressRepository.save(adressEntity);

        UserEntity userEntity = new UserEntity();

        userEntity.setAdressEntity(adressEntity);
        userEntity.setEmail(form.getEmail());
        userEntity.setFirstName(form.getFirstName());
        userEntity.setLastName(form.getLastName());
        userEntity.setBirthdate(form.getBirthdate());
        userEntity.setPhone(form.getPhone());

        String login = form.getLastName().substring(0,3).concat(form.getFirstName().substring(0,3)).concat(String.valueOf(form.getBirthdate().getDayOfYear()));

        userEntity.setLogin(login);
        userEntity.setPassword( passwordEncoder.encode(form.getPassword()) );
        userEntity.getRoles().add(Role.STUDENT);
        userEntity = userRepository.save(userEntity);

        String texte = " Bonjour,\n" +
                "votre compte a bien été créé.\n" +
                "Votre compte est maintenant en attente de validation";
        //emailService.sendMessage(userEntity.getEmail(), "Compte créé", texte);

    }

    @Override
    public boolean checkEmailNotTaken(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public Auth login(LoginForm form) {
        System.out.println("Service:"+ form);
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(form.getLogin(),form.getPassword()) );

        UserEntity userEntity = userRepository.findByLogin(form.getLogin() )
                .orElseThrow();

        String token = jwtProvider.generateToken(userEntity.getUsername(), List.copyOf(userEntity.getRoles()) );

        return Auth.builder()
                .token(token)
                .login(userEntity.getLogin())
                .roles(userEntity.getRoles())
                .build();
    }

    @Override
    public Long findByLogin(String login) {
        return userRepository.findByLogin(login).get().getId();
    }

    @Override
    public void validate(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        String texte = " Bonjour,\n" +
                "votre compte a bien été validé.\n" +
                "Vous pouvez dès lors vous connecter à votre compte";
        emailService.sendMessage(userEntity.getEmail(), "Compte validé", texte);
    }

    @Override
    public void unValidate(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        userEntity.setEnabled(false);
        userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> getAllUnvalidate() {
        return userRepository.getAllUnvalidate().stream()
                .collect(Collectors.toList());
    }

    @Override
    public void sendPasswordMail(String login) {
        UserEntity userEntity = userRepository.findByLogin(login).get();

        if(resetValidatorRepository.existsByLogin(userRepository.findByLogin(login).get().getLogin()))
            resetValidatorRepository.deleteById(resetValidatorRepository.findByLogin(login).getId());

        String texte = " Bonjour,\n" +
                "Veuillez vous rendre ici pour changer de mot de passe: http://localhost:8080/swagger \n" +
                "Si vous n'avez pas fait cette demande, veuillez ignorer ce mail";

        //emailService.sendMessage(userEntity.getEmail(), "reset mot de passe", texte);

        ResetValidatorEntity resetValidatorEntity = new ResetValidatorEntity();
        resetValidatorEntity.setResetTime(LocalDateTime.now());
        resetValidatorEntity.setLogin(login);

        resetValidatorRepository.save(resetValidatorEntity);
    }

    @Override
    public void resetPassword(String password, String email) {

        if(resetValidatorRepository.existsByLogin(userRepository.findByEmail(email).get().getLogin()) && (LocalDateTime.now().isBefore(resetValidatorRepository.findByLogin(userRepository.findByEmail(email).get().getLogin()).getResetTime().plusMinutes(10)))){
            UserEntity userEntity = userRepository.findByEmail(email).get();
            userEntity.setPassword(passwordEncoder.encode(password));
            userRepository.save(userEntity);
            resetValidatorRepository.deleteById(resetValidatorRepository.findByLogin(userRepository.findByEmail(email).get().getLogin()).getId());

        }else if(resetValidatorRepository.existsByLogin(userRepository.findByEmail(email).get().getLogin()))
            resetValidatorRepository.deleteById(resetValidatorRepository.findByLogin(userRepository.findByEmail(email).get().getLogin()).getId());

    }

}

package be.technobel.ylorth.reservastock_rest.bll.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.EmailAlreadyTakenException;
import be.technobel.ylorth.reservastock_rest.dal.models.Adress;
import be.technobel.ylorth.reservastock_rest.dal.models.ResetValidator;
import be.technobel.ylorth.reservastock_rest.dal.models.Role;
import be.technobel.ylorth.reservastock_rest.dal.models.User;
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
import be.technobel.ylorth.reservastock_rest.pl.models.AuthDTO;
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

        Adress adress = new Adress();

        adress.setNumber(form.getNumber());
        adress.setStreet(form.getStreet());
        adress.setCountry(form.getCountry());
        adress.setPostCode(form.getPostCode());
        adress.setCity(form.getCity());

        adress = adressRepository.save(adress);

        User user = new User();

        user.setAdress(adress);
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setBirthdate(form.getBirthdate());
        user.setPhone(form.getPhone());

        String login = form.getLastName().substring(0,3).concat(form.getFirstName().substring(0,3)).concat(String.valueOf(form.getBirthdate().getDayOfYear()));

        user.setEnabled(true);
        user.setLogin(login);
        user.setRoles(form.getRoles());
        user.setPassword( passwordEncoder.encode(form.getPassword()) );
        user = userRepository.save( user );
    }

    @Override
    public void registerStudent(RegisterForm form) {
        if( userRepository.existsByEmail(form.getEmail()))
            throw new EmailAlreadyTakenException();

        Adress adress = new Adress();

        adress.setNumber(form.getNumber());
        adress.setStreet(form.getStreet());
        adress.setCountry(form.getCountry());
        adress.setPostCode(form.getPostCode());
        adress.setCity(form.getCity());

        adress = adressRepository.save(adress);

        User user = new User();

        user.setAdress(adress);
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setBirthdate(form.getBirthdate());
        user.setPhone(form.getPhone());

        String login = form.getLastName().substring(0,3).concat(form.getFirstName().substring(0,3)).concat(String.valueOf(form.getBirthdate().getDayOfYear()));

        user.setLogin(login);
        user.setPassword( passwordEncoder.encode(form.getPassword()) );
        user.getRoles().add(Role.STUDENT);
        user = userRepository.save( user );

        String texte = " Bonjour,\n" +
                "votre compte a bien été créé.\n" +
                "Votre compte est maintenant en attente de validation";
        //emailService.sendMessage(user.getEmail(), "Compte créé", texte);

    }

    @Override
    public boolean checkEmailNotTaken(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public AuthDTO login(LoginForm form) {
        System.out.println("Service:"+ form);
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(form.getLogin(),form.getPassword()) );

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
        user.setEnabled(true);
        userRepository.save(user);
        String texte = " Bonjour,\n" +
                "votre compte a bien été validé.\n" +
                "Vous pouvez dès lors vous connecter à votre compte";
        emailService.sendMessage(user.getEmail(), "Compte validé", texte);
    }

    @Override
    public void unValidate(Long id) {
        User user = userRepository.findById(id).get();
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUnvalidate() {
        return userRepository.getAllUnvalidate().stream()
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

        //emailService.sendMessage(user.getEmail(), "reset mot de passe", texte);

        ResetValidator resetValidator = new ResetValidator();
        resetValidator.setResetTime(LocalDateTime.now());
        resetValidator.setLogin(login);

        resetValidatorRepository.save(resetValidator);
    }

    @Override
    public void resetPassword(String password, String email) {

        if(resetValidatorRepository.existsByLogin(userRepository.findByEmail(email).get().getLogin()) && (LocalDateTime.now().isBefore(resetValidatorRepository.findByLogin(userRepository.findByEmail(email).get().getLogin()).getResetTime().plusMinutes(10)))){
            User user = userRepository.findByEmail(email).get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            resetValidatorRepository.deleteById(resetValidatorRepository.findByLogin(userRepository.findByEmail(email).get().getLogin()).getId());

        }else if(resetValidatorRepository.existsByLogin(userRepository.findByEmail(email).get().getLogin()))
            resetValidatorRepository.deleteById(resetValidatorRepository.findByLogin(userRepository.findByEmail(email).get().getLogin()).getId());

    }

}

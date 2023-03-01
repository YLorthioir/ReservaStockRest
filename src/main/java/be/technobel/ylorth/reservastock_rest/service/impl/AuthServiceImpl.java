package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.EmailAlreadyTakenException;
import be.technobel.ylorth.reservastock_rest.model.entity.Role;
import be.technobel.ylorth.reservastock_rest.model.entity.User;
import be.technobel.ylorth.reservastock_rest.model.form.LoginForm;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import be.technobel.ylorth.reservastock_rest.model.form.StudentRegisterForm;
import be.technobel.ylorth.reservastock_rest.repository.UserRepository;
import be.technobel.ylorth.reservastock_rest.service.AuthService;
import be.technobel.ylorth.reservastock_rest.service.mapper.UserMapper;
import be.technobel.ylorth.reservastock_rest.utils.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import be.technobel.ylorth.reservastock_rest.model.dto.AuthDTO;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthDTO register(RegisterForm form) {
        if( userRepository.existsByEmail(form.getEmail()))
            throw new EmailAlreadyTakenException();

        User user = userMapper.toEntity(form);
        user.setMotDePasse( passwordEncoder.encode(form.getMotDePasse()) );
        user = userRepository.save( user );

        String token = jwtProvider.generateToken(user.getLogin(), user.getRole());

        return AuthDTO.builder()
                .token(token)
                .login(user.getLogin())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthDTO register(StudentRegisterForm form) {
        if( userRepository.existsByEmail(form.getEmail()))
            throw new EmailAlreadyTakenException();

        User user = userMapper.toEntity(form);
        user.setMotDePasse( passwordEncoder.encode(form.getMotDePasse()) );
        user = userRepository.save( user );

        String token = jwtProvider.generateToken(user.getLogin(), user.getRole());

        return AuthDTO.builder()
                .token(token)
                .login(user.getLogin())
                .role(Role.ETUDIANT)
                .build();
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

        String token = jwtProvider.generateToken(user.getUsername(), user.getRole() );

        return AuthDTO.builder()
                .token(token)
                .login(user.getLogin())
                .role(user.getRole())
                .build();
    }
}
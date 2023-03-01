package be.technobel.ylorth.reservastock_rest.service.impl;

import be.technobel.ylorth.reservastock_rest.exception.EmailAlreadyTakenException;
import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.model.dto.UserDTO;
import be.technobel.ylorth.reservastock_rest.model.entity.User;
import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import be.technobel.ylorth.reservastock_rest.repository.UserRepository;
import be.technobel.ylorth.reservastock_rest.service.UserService;
import be.technobel.ylorth.reservastock_rest.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void register(RegisterForm form) {
        if( userRepository.existsByEmail(form.getEmail()))
            throw new EmailAlreadyTakenException();

        User user = userMapper.toEntity(form);
        user = userRepository.save( user );
    }

    @Override
    public void update(RegisterForm form) {
        if( userRepository.existsByEmail(form.getEmail()))
            throw new EmailAlreadyTakenException();

        User user = userMapper.toEntity(form);
        user = userRepository.save( user );
    }

    @Override
    public UserDTO getOne(Long id) {
        return userRepository.findById(id)
                .map( userMapper::toDTO )
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public boolean checkEmailNotTaken(String email) {
        return !userRepository.existsByEmail(email);
    }
}

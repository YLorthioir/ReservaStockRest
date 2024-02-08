package be.technobel.ylorth.reservastock_rest.dal.tools;

import be.technobel.ylorth.reservastock_rest.dal.models.*;
import be.technobel.ylorth.reservastock_rest.dal.repository.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Log4j2
public class DataInit implements InitializingBean {
    private final AdressRepository adressRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    private final RoomRepository roomRepository;
    private final MaterialRepository materialRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInit(RoomRepository roomRepository, MaterialRepository materialRepository,
                    UserRepository userRepository,
                    RequestRepository requestRepository,
                    PasswordEncoder passwordEncoder,
                    AdressRepository adressRepository) {
        this.roomRepository = roomRepository;
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.passwordEncoder = passwordEncoder;
        this.adressRepository = adressRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        log.info("-- INITIALIZING DB DATA --");

        MaterialEntity materialEntity1 = new MaterialEntity();
        materialEntity1.setName("Interactive whiteboard");
        materialEntity1 = materialRepository.save(materialEntity1);

        MaterialEntity materialEntity2 = new MaterialEntity();
        materialEntity2.setName("Projector");
        materialEntity2 = materialRepository.save(materialEntity2);

        MaterialEntity materialEntity3 = new MaterialEntity();
        materialEntity3.setName("TV");
        materialEntity3 = materialRepository.save(materialEntity3);

        MaterialEntity materialEntity4 = new MaterialEntity();
        materialEntity4.setName("Computer");
        materialEntity4 = materialRepository.save(materialEntity4);

        RoomEntity roomEntity1 = new RoomEntity();
        roomEntity1.setName("Sonic");
        roomEntity1.setCapacity(20);
        roomEntity1.setForStaff(false);
        Set<MaterialEntity> set1 = new LinkedHashSet<>();
        set1.add(materialEntity1);
        set1.add(materialEntity2);
        roomEntity1.setContains(set1);

        roomRepository.save(roomEntity1);

        RoomEntity roomEntity2 = new RoomEntity();
        roomEntity2.setName("Mario");
        roomEntity2.setCapacity(60);
        roomEntity2.setForStaff(true);
        Set<MaterialEntity> set2 = new LinkedHashSet<>();
        set2.add(materialEntity3);
        set2.add(materialEntity4);
        roomEntity2.setContains(set2);

        roomRepository.save(roomEntity2);

        RoomEntity roomEntity3 = new RoomEntity();
        roomEntity3.setName("Donkey kong");
        roomEntity3.setCapacity(20);
        roomEntity3.setForStaff(false);
        Set<MaterialEntity> set3 = new LinkedHashSet<>();
        set3.add(materialEntity1);
        set3.add(materialEntity2);
        roomEntity3.setContains(set3);

        roomRepository.save(roomEntity3);

        AdressEntity addresseUser1 = new AdressEntity();
        addresseUser1.setNumber("1");
        addresseUser1.setStreet("rue du blabla");
        addresseUser1.setPostCode(5000);
        addresseUser1.setCity("Namur");
        addresseUser1.setCountry("Belgique");

        addresseUser1 = adressRepository.save(addresseUser1);

        UserEntity userEntity1 = new UserEntity();
        Set<Role> setRole1 = new HashSet<>();
        setRole1.add(Role.STUDENT);
        userEntity1.setLastName("Dupont");
        userEntity1.setFirstName("Jean");
        userEntity1.setRoles(setRole1);
        userEntity1.setPhone("0123/2344.233");
        userEntity1.setAdressEntity(addresseUser1);
        userEntity1.setBirthdate(LocalDate.now());
        userEntity1.setLogin("login");
        userEntity1.setPassword(passwordEncoder.encode("Test1234="));
        userEntity1.setEmail("test@test.be");
        userEntity1.setEnabled(true);

        userEntity1 = userRepository.save(userEntity1);

        AdressEntity addresseUser2 = new AdressEntity();
        addresseUser2.setNumber("3");
        addresseUser2.setStreet("rue du blablo");
        addresseUser2.setPostCode(5001);
        addresseUser2.setCity("Jambes");
        addresseUser2.setCountry("Belgique");

        addresseUser2 = adressRepository.save(addresseUser2);

        UserEntity userEntity2 = new UserEntity();
        Set<Role> setRole2 = new HashSet<>();
        setRole2.add(Role.PROFESSOR);
        userEntity2.setLastName("Marc");
        userEntity2.setFirstName("Pierre");
        userEntity2.setRoles(setRole2);
        userEntity2.setPhone("0123/234.233");
        userEntity2.setAdressEntity(addresseUser2);
        userEntity2.setBirthdate(LocalDate.of(1981,11,19));
        userEntity2.setLogin("login2");
        userEntity2.setPassword(passwordEncoder.encode("Test1234="));
        userEntity2.setEmail("test@test.Com");
        userEntity2.setEnabled(true);

        userEntity2 = userRepository.save(userEntity2);

        AdressEntity addresseUser3 = new AdressEntity();
        addresseUser3.setNumber("3");
        addresseUser3.setStreet("Chemin de la bioce");
        addresseUser3.setPostCode(6833);
        addresseUser3.setCity("Ucimont");
        addresseUser3.setCountry("Belgique");

        addresseUser3 = adressRepository.save(addresseUser3);

        UserEntity userEntity3 = new UserEntity();
        Set<Role> setRole3 = new HashSet<>();
        setRole3.add(Role.ADMIN);
        userEntity3.setLastName("Lorthioir");
        userEntity3.setFirstName("Yann");
        userEntity3.setRoles(setRole3);
        userEntity3.setPhone("0123/2333333");
        userEntity3.setAdressEntity(addresseUser3);
        userEntity3.setBirthdate(LocalDate.of(1991,03,13));
        userEntity3.setLogin("admin");
        userEntity3.setPassword(passwordEncoder.encode("Test1234="));
        userEntity3.setEmail("yann.lorthioir@edu.technobel.be");
        userEntity3.setEnabled(true);


        userEntity3 = userRepository.save(userEntity3);

        RequestEntity requestEntity1 = new RequestEntity();
        requestEntity1.setRoomEntity(roomEntity1);
        requestEntity1.setMaterialEntities(set1);
        requestEntity1.setUserEntity(userEntity1);
        requestEntity1.setAdmin(userEntity3);
        requestEntity1.setStartTime(LocalDateTime.now());
        requestEntity1.setMinutes(30);
        requestEntity1.setRequestReason("Test 1");


        requestEntity1 = requestRepository.save(requestEntity1);

        RequestEntity requestEntity2 = new RequestEntity();
        requestEntity2.setRoomEntity(roomEntity1);
        requestEntity2.setMaterialEntities(set1);
        requestEntity2.setUserEntity(userEntity1);
        requestEntity2.setStartTime(LocalDateTime.now().plusHours(1));
        requestEntity2.setMinutes(30);
        requestEntity2.setRequestReason("Test 2");

        requestEntity2 = requestRepository.save(requestEntity2);

        RequestEntity requestEntity3 = new RequestEntity();
        requestEntity3.setRoomEntity(roomEntity1);
        requestEntity3.setMaterialEntities(set1);
        requestEntity3.setUserEntity(userEntity1);
        requestEntity3.setStartTime(LocalDateTime.now().plusHours(2));
        requestEntity3.setMinutes(30);
        requestEntity3.setRequestReason("Test 3");
        requestEntity3.setRefusalReason("Refusal test");

        requestEntity3 = requestRepository.save(requestEntity3);

        RequestEntity requestEntity4 = new RequestEntity();
        requestEntity4.setRoomEntity(roomEntity1);
        requestEntity4.setMaterialEntities(set1);
        requestEntity4.setUserEntity(userEntity1);
        requestEntity4.setAdmin(userEntity3);
        requestEntity4.setStartTime(LocalDateTime.now().plusHours(3));
        requestEntity4.setMinutes(30);
        requestEntity4.setRequestReason("Test 4");

        requestEntity4 = requestRepository.save(requestEntity4);

        RequestEntity requestEntity5 = new RequestEntity();
        requestEntity5.setRoomEntity(roomEntity3);
        requestEntity5.setMaterialEntities(set1);
        requestEntity5.setUserEntity(userEntity2);
        requestEntity5.setAdmin(userEntity3);
        requestEntity5.setStartTime(LocalDateTime.now());
        requestEntity5.setMinutes(20);
        requestEntity5.setRequestReason("RoomEntity prof test 1");

        requestEntity5 = requestRepository.save(requestEntity5);

        log.info("-- DATA INIT FINISHED --");
    }

}

package be.technobel.ylorth.reservastock_rest.utils;

import be.technobel.ylorth.reservastock_rest.model.entity.*;
import be.technobel.ylorth.reservastock_rest.repository.*;
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

        Material material1 = new Material();
        material1.setName("Tableau interactif");
        material1 = materialRepository.save(material1);

        Material material2 = new Material();
        material2.setName("Projecteur");
        material2 = materialRepository.save(material2);

        Material material3 = new Material();
        material3.setName("Télévision");
        material3 = materialRepository.save(material3);

        Material material4 = new Material();
        material4.setName("Ordinateurs");
        material4 = materialRepository.save(material4);

        Room room1 = new Room();
        room1.setName("Sonic");
        room1.setCapacity(20);
        room1.setForStaff(false);
        Set<Material> set1 = new LinkedHashSet<>();
        set1.add(material1);
        set1.add(material2);
        room1.setContains(set1);

        roomRepository.save(room1);

        Room room2 = new Room();
        room2.setName("Mario");
        room2.setCapacity(60);
        room2.setForStaff(true);
        Set<Material> set2 = new LinkedHashSet<>();
        set2.add(material3);
        set2.add(material4);
        room2.setContains(set2);

        roomRepository.save(room2);

        Room room3 = new Room();
        room3.setName("Donkey kong");
        room3.setCapacity(20);
        room3.setForStaff(false);
        Set<Material> set3 = new LinkedHashSet<>();
        set3.add(material1);
        set3.add(material2);
        room3.setContains(set3);

        roomRepository.save(room3);

        Adress addresseUser1 = new Adress();
        addresseUser1.setNumber("1");
        addresseUser1.setStreet("rue du blabla");
        addresseUser1.setPostCode(5000);
        addresseUser1.setCity("Namur");
        addresseUser1.setCountry("Belgique");

        addresseUser1 = adressRepository.save(addresseUser1);

        User user1 = new User();
        Set<Role> setRole1 = new HashSet<>();
        setRole1.add(Role.STUDENT);
        user1.setLastname("Dupont");
        user1.setFirstname("Jean");
        user1.setRoles(setRole1);
        user1.setPhone("0123/2344.233");
        user1.setAdress(addresseUser1);
        user1.setBirthdate(LocalDate.now());
        user1.setLogin("login");
        user1.setPassword(passwordEncoder.encode("Test1234="));
        user1.setEmail("test@test.be");
        user1.setEnabled(true);

        user1 = userRepository.save(user1);

        Adress addresseUser2 = new Adress();
        addresseUser2.setNumber("3");
        addresseUser2.setStreet("rue du blablo");
        addresseUser2.setPostCode(5001);
        addresseUser2.setCity("Jambes");
        addresseUser2.setCountry("Belgique");

        addresseUser2 = adressRepository.save(addresseUser2);

        User user2 = new User();
        Set<Role> setRole2 = new HashSet<>();
        setRole2.add(Role.PROFESSOR);
        user2.setLastname("Marc");
        user2.setFirstname("Pierre");
        user2.setRoles(setRole2);
        user2.setPhone("0123/234.233");
        user2.setAdress(addresseUser2);
        user2.setBirthdate(LocalDate.of(1981,11,19));
        user2.setLogin("login2");
        user2.setPassword(passwordEncoder.encode("Test1234="));
        user2.setEmail("test@test.Com");
        user2.setEnabled(true);

        user2 = userRepository.save(user2);

        Adress addresseUser3 = new Adress();
        addresseUser3.setNumber("3");
        addresseUser3.setStreet("Chemin de la bioce");
        addresseUser3.setPostCode(6833);
        addresseUser3.setCity("Ucimont");
        addresseUser3.setCountry("Belgique");

        addresseUser3 = adressRepository.save(addresseUser3);

        User user3 = new User();
        Set<Role> setRole3 = new HashSet<>();
        setRole3.add(Role.ADMIN);
        user3.setLastname("Lorthioir");
        user3.setFirstname("Yann");
        user3.setRoles(setRole3);
        user3.setPhone("0123/2333333");
        user3.setAdress(addresseUser3);
        user3.setBirthdate(LocalDate.of(1991,03,13));
        user3.setLogin("admin");
        user3.setPassword(passwordEncoder.encode("Test1234="));
        user3.setEmail("yann.lorthioir@edu.technobel.be");
        user3.setEnabled(true);


        user3 = userRepository.save(user3);

        Request request1 = new Request();
        request1.setRoom(room1);
        request1.setMaterials(set1);
        request1.setUser(user1);
        request1.setAdmin(user3);
        request1.setStartTime(LocalDateTime.now());
        request1.setMinutes(30);
        request1.setRequestReason("Test 1");


        request1 = requestRepository.save(request1);

        Request request2 = new Request();
        request2.setRoom(room1);
        request2.setMaterials(set1);
        request2.setUser(user1);
        request2.setStartTime(LocalDateTime.now().plusHours(1));
        request2.setMinutes(30);
        request2.setRequestReason("Test 2");

        request2 = requestRepository.save(request2);

        Request request3 = new Request();
        request3.setRoom(room1);
        request3.setMaterials(set1);
        request3.setUser(user1);
        request3.setStartTime(LocalDateTime.now().plusHours(2));
        request3.setMinutes(30);
        request3.setRequestReason("Test 3");
        request3.setRefusalReason("Refusal test");

        request3 = requestRepository.save(request3);

        Request request4 = new Request();
        request4.setRoom(room1);
        request4.setMaterials(set1);
        request4.setUser(user1);
        request4.setAdmin(user3);
        request4.setStartTime(LocalDateTime.now().plusHours(3));
        request4.setMinutes(30);
        request4.setRequestReason("Test 4");

        request4 = requestRepository.save(request4);

        Request request5 = new Request();
        request5.setRoom(room3);
        request5.setMaterials(set1);
        request5.setUser(user2);
        request5.setAdmin(user3);
        request5.setStartTime(LocalDateTime.now());
        request5.setMinutes(20);
        request5.setRequestReason("Room prof test");

        request5 = requestRepository.save(request5);

        log.info("-- DATA INIT FINISHED --");
    }

}

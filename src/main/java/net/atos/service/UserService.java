package net.atos.service;


import net.atos.model.Order;
import net.atos.model.Role;
import net.atos.model.User;
import net.atos.model.dto.UserDto;
import net.atos.repository.RoleRepository;
import net.atos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService
{
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User changePassword(String email, String password) {
        User user = userRepository.findFirstByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return  userRepository.save(user);
    }


    //rejestracja nowego uzytkownika
    public User registerUser(UserDto userDto) {
        User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),passwordEncoder.encode(userDto.getPassword()));
        user.addRole(roleRepository.getOne(1L));
        return userRepository.save(user);
    }


    //lista wszystkich mejli
    public List<String> getAllEmails()
    {
        return userRepository.findAllEmails();
    }

    //lista wszystkich uzytkownikow
    public Page <User> getAllUsers(String value, Pageable pageable)
    {
        return userRepository.findAllByFirstNameContainsOrLastNameContainsOrEmailContains(value,value,value, pageable);
    }


    //usuwam wybranego uzytkownika
    public User deleteUser(Long id) {
        User user = userRepository.getOne(id);
        userRepository.delete(user);
        return user;
    }

    //wybieram uzytkownika po id
    public User getUserById(Long id) {
        User user = userRepository.getOne(id);
        return user;

    }

    //zmiana danych
    public User updateUserData(Long id,String firstName, String lastName, String email) {
        User user = getUserById(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userRepository.save(user);
        return user;
    }

    public User updateUserDataNew(Long id,String firstName, String lastName, String city, Integer mobileNumber) {
        User user = getUserById(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCity(city);
        user.setMobileNumber(mobileNumber);
        userRepository.save(user);
        return user;
    }

    //znajdz uzytkownika po mejlu
    public User findUserByEmail(String email) {
        User user = userRepository.findFirstByEmail(email);
        return user;
    }


    public PagedListHolder <Order> getOrdersAssignedToUser(User user) {
        List <Order> orderUserList = user.getOrder().stream()
                .sorted(Comparator.comparing(Order::getReturnDate)
                .reversed())
                .collect(Collectors.toList());

        PagedListHolder <Order> orderPage = new PagedListHolder<>(orderUserList);
        //return orderUserList;
        return orderPage;
    }

    //zmiana statusu uzytkownika
    public User changeUserStatus(User user) {
        user.setStatus(!user.isStatus());
        userRepository.save(user);
        return user;
    }

    //czy konto jest aktywne
    public boolean accountIsActive(String email) {
        User user = findUserByEmail(email);
        return user.isStatus();
    }

}

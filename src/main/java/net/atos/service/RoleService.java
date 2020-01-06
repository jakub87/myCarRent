package net.atos.service;


import net.atos.model.User;
import net.atos.repository.RoleRepository;
import net.atos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    //dodanie roli adminowej //rola id 2 to rola adminowa
    public User addAdminRole (User user) {
        //Role adminRole = roleRepository.getOne(2L);
        user.addRole(roleRepository.getOne(2L));
        userRepository.save(user);
        return user;
    }

    //zabranie roli adminowej
    public User removeAdminRole(User user) {
        //Role adminRole = roleRepository.getOne(2L);
        user.removeRole(roleRepository.getOne(2L));
        userRepository.save(user);
        return user;
    }

}

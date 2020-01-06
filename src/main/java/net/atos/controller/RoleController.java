package net.atos.controller;


import net.atos.model.User;
import net.atos.repository.UserRepository;
import net.atos.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RoleController {

    private RoleService roleService;
    private UserRepository userRepository;

    @Autowired
    public RoleController(RoleService roleService, UserRepository userRepository) {
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    //dodajemy role adminowa
    @PostMapping("/roles/addAdminRole/{user_id}")
    public String addAdminRole(@PathVariable ("user_id") Long user_id) {
        User user = userRepository.getOne(user_id);
        roleService.addAdminRole(user);
        return "redirect:/users/list";

    }

    //zabieramy role adminowa
    @PostMapping("roles/removeAdminRole/{user_id}")
    public String removeAdminRole(@PathVariable ("user_id") Long user_id) {
        User user = userRepository.getOne(user_id);
        roleService.removeAdminRole(user);
        return "redirect:/users/list";
    }

}

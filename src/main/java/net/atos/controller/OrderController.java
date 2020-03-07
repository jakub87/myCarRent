package net.atos.controller;

import net.atos.model.Order;
import net.atos.model.User;
import net.atos.service.LoginService;
import net.atos.service.OrderService;
import net.atos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class OrderController {

    private OrderService orderService;
    private LoginService loginService;
    private UserService userService;

    @Autowired
    public OrderController(OrderService orderService, LoginService loginService, UserService userService) {
        this.orderService = orderService;
        this.loginService = loginService;
        this.userService = userService;
    }

    @GetMapping(value = {"/orders","/orders/{admin}"}) // new version
    public String getAllReservation(@RequestParam (value = "pageSize", defaultValue = "5") int pageSize,
                                    @RequestParam (value = "pageNumber", defaultValue = "0") int pageNumber,
                                    @RequestParam (value = "valueToFind", defaultValue = "") String valueToFind,
                                    @PathVariable (value = "admin", required = false) String admin,
                                    Authentication auth,
                                    Model model) {
        Optional <String> isAdmin = Optional.ofNullable(admin);
        boolean isAdminURL = false;
        PagedListHolder<Order> orderList;

        if (isAdmin.isPresent()) {
            orderList = orderService.getAllReservation(valueToFind);
            isAdminURL = true;
        }else {
            User user = userService.findUserByEmail(loginService.getLoginFromCredentials(auth));
            orderList = userService.getOrdersAssignedToUser(user);
        }

        orderList.setPage(pageNumber);
        orderList.setPageSize(pageSize);
        model.addAttribute("orderList",orderList);

        model.addAttribute("isAdminURL",isAdminURL);
        model.addAttribute("selectedPageSize",pageSize);
        model.addAttribute("logged_email",loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));
        return "userOrder";
    }

    @PostMapping( value = {"/orders/cancel/{order_id}" ,"orders/cancel/{order_id}/{admin}"} )
    public String cancelOrder(@PathVariable ("order_id") Long order_id,
                              @PathVariable (value = "admin", required = false) String admin,
                              Authentication auth) {
        Optional<String> isAdmin = Optional.ofNullable(admin);

        orderService.updateOrder(order_id);

        if(isAdmin.isPresent()) {
            return "redirect:/orders/admin";
        }else {
            return "redirect:/orders";
        }
    }
}

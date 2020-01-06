package net.atos.controller;

import net.atos.model.Car;
import net.atos.model.Order;
import net.atos.model.User;
import net.atos.model.dto.OrderDto;
import net.atos.model.dto.UserDto;
import net.atos.service.CarService;
import net.atos.service.LoginService;
import net.atos.service.OrderService;
import net.atos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Controller
public class UserController
{
    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private LoginService loginService;

    @Autowired
    public UserController(UserService userService, CarService carService, OrderService orderService, LoginService loginService) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.loginService = loginService;
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("userDto",new UserDto());
        return "registerUser";
    }


    @PostMapping("/registration")
    public String registrationResult(@ModelAttribute @Valid UserDto userDto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            return "registerUser";
        }

        if (userService.getAllEmails().contains(userDto.getEmail())) {
            model.addAttribute("existingEmail","Provided email already exists");
            return "registerUser";
        }

        userService.registerUser(userDto);
        return "redirect:/";
    }

    @GetMapping("/users/list") //lista uzytkownikow
    public String showAllUsers(
                       @RequestParam (value = "pageSize", defaultValue = "5") int pageSize,
                       @RequestParam (value = "pageNumber", defaultValue = "0") int pageNumber,
                       @RequestParam (value = "value", defaultValue = "") String value,
                       Authentication auth,
                        Model model) {

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page <User> pageUsers = userService.getAllUsers(value, pageable);

         model.addAttribute("selectedPageSize",pageSize);
        model.addAttribute("pageUsers",pageUsers);
        model.addAttribute("logged_email",loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));

        return "listOfUsers";
    }


    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable ("id") Long id)
    {
        userService.deleteUser(id);
        return "redirect:/users/list";
    }

    @GetMapping(value = {"/users/userdetails","/users/userdetails/{user_id}"})
    public String getUserDetails(@PathVariable (value = "user_id", required = false) Long user_id,
                                 Model model,
                                 Authentication auth) {
        User user;
        Optional<Long> getUserid = Optional.ofNullable(user_id);

        if (getUserid.isPresent()) {
            user = userService.getUserById(user_id);
        } else {
            user = userService.findUserByEmail(loginService.getLoginFromCredentials(auth));
        }
        model.addAttribute("user", user);
        model.addAttribute("logged_email",loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));
        return "userDetails";
    }

    @PostMapping("/users/userdetails/{id}")
    public String updateUserData(@ModelAttribute @Valid UserDto userDto,
                                 BindingResult bindingResult,
                                 @PathVariable ("id") Long id,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error","Incorrect value in field "+bindingResult.getFieldError().getField()+ "!");
            return "redirect:/users/userdetails";
        }

        userService.updateUserDataNew(id,userDto.getFirstName(),userDto.getLastName(),userDto.getCity(),userDto.getMobileNumber());
        redirectAttributes.addFlashAttribute("result","Your data have been updated.");

        return "redirect:/users/userdetails";
    }

    @PostMapping("/user/makeorder/{id}")
    public String makeOrder(@ModelAttribute OrderDto orderDto,
                            @PathVariable ("id") Long id,
                            Authentication auth,
                            RedirectAttributes redirectAttributes) {


            if (orderDto.getStartDate() == null || orderDto.getEndDate() == null) { // ktoras z dat jest pusta
                redirectAttributes.addFlashAttribute("emptyDates", "emptyDates");
                return "redirect:/detailsofcar/"+id;
            }

            if (orderDto.getStartDate().isBefore(LocalDate.now())) { //start date jest w przeszlosci
                redirectAttributes.addFlashAttribute("startDateInPast", "startDateInPast");
                return "redirect:/detailsofcar/"+id;
            }

            if (orderDto.getEndDate().isBefore(LocalDate.now())) { // end date jest przeszlosci
                redirectAttributes.addFlashAttribute("endDateInPast", "endDateInPast");
                return "redirect:/detailsofcar/"+id;
            }
            if (orderDto.getStartDate().isAfter(orderDto.getEndDate())) {// start date jest pozniej niz end date
                redirectAttributes.addFlashAttribute("startDateLaterThanEndDate", "startDateLaterThanEndDate");
                return "redirect:/detailsofcar/"+id;
            }

            Car car = carService.getCarById(id);
            String emailOfUser = loginService.getLoginFromCredentials(auth);

            User user = userService.findUserByEmail(emailOfUser);

            if (!orderService.checkAvailabilityOfCar(id,orderDto.getStartDate(),orderDto.getEndDate())) {//jesli termin jest zajety
                redirectAttributes.addFlashAttribute("occupied", "Between "+orderDto.getStartDate()+" and "+orderDto.getEndDate()+ " vehicle is busy.");
                return "redirect:/detailsofcar/"+id;
            }

        //tworzenie zamowienia
        Period period = Period.between(orderDto.getStartDate(),orderDto.getEndDate());
        int daysRent  = period.getDays() + 1; // ilosc dni na ile wybieramy pojazd

        BigDecimal rentPrice = car.getPrice().multiply(BigDecimal.valueOf(daysRent));
        OrderDto carReservation = new OrderDto(user,car,orderDto.getStartDate(),orderDto.getEndDate(),rentPrice);

        orderService.addNewOrder(carReservation);
        redirectAttributes.addFlashAttribute("success", "success");

        return "redirect:/detailsofcar/"+id;
    }

    @GetMapping("/users/userorders")
    public String getOrdersAssignedToUser(@RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                          @RequestParam (value = "pageNumber", defaultValue = "0") int pageNumber,
                                          Authentication auth,
                                          Model model) {
        User user = userService.findUserByEmail(loginService.getLoginFromCredentials(auth));

        PagedListHolder <Order> orderUserList = userService.getOrdersAssignedToUser(user);
        orderUserList.setPage(pageNumber);
        orderUserList.setPageSize(pageSize);

        model.addAttribute("orderUserList",orderUserList);
        model.addAttribute("selectedPageSize",pageSize);
        model.addAttribute("logged_email",loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));
        return "userOrder";
    }

    //zmiana statusu uzytkownika
    @PostMapping("/users/changeuserstatus/{user_id}")
    public String changeUserStatus(@PathVariable ("user_id") Long user_id) {
        User user = userService.getUserById(user_id);
        userService.changeUserStatus(user);
        return "redirect:/users/list";
    }

}

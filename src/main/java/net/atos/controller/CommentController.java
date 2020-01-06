package net.atos.controller;

import net.atos.model.Car;
import net.atos.model.User;
import net.atos.model.dto.CommentDto;
import net.atos.service.CarService;
import net.atos.service.CommentService;

import net.atos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CommentController {

    private CommentService commentService;
    private CarService carService;
    private UserService userService;

    @Autowired
    public CommentController(CommentService commentService, CarService carService, UserService userService) {
        this.commentService = commentService;
        this.carService = carService;
        this.userService = userService;
    }

    @PostMapping("/message/add/{car_id}&{logged_email}") //dodanie komentarza dla pojazdu
    public String addMessage(@ModelAttribute CommentDto commentDto,
                             @PathVariable ("car_id") Long car_id,
                             @PathVariable ("logged_email") String loggedEmail)

    {
        User user = userService.findUserByEmail(loggedEmail);
        Car car = carService.getCarById(car_id);
        commentService.addComment(new CommentDto(car,user,commentDto.getContent()));

        return "redirect:/detailsofcar/"+car_id;
    }

}

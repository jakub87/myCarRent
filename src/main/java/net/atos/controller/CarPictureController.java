package net.atos.controller;

import net.atos.service.CarPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CarPictureController {

    private CarPictureService carPictureService;

    @Autowired
    public CarPictureController(CarPictureService carPictureService) {
        this.carPictureService = carPictureService;
    }

    @PostMapping("/carpicture/delete&{car_id}&{carPicture_id}")
    public String deleteImageFromCar(@PathVariable("car_id") Long car_id,
                                     @PathVariable ("carPicture_id") Long carPicture_id,
                                     RedirectAttributes redirectAttributes)
    {

        carPictureService.deleteImageFromCar(carPicture_id);
        redirectAttributes.addFlashAttribute("result","Image has been deleted!");
        return "redirect:/cars/edit/"+car_id;
    }


}

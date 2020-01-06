package net.atos.controller;


import net.atos.model.Equipments;
import net.atos.model.dto.EquipmentsDto;
import net.atos.service.EquipmentsService;
import net.atos.service.LoginService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EquipmentsController {

    private EquipmentsService equipmentsService;
    private LoginService loginService;

    public EquipmentsController(EquipmentsService equipmentsService, LoginService loginService) {
        this.equipmentsService = equipmentsService;
        this.loginService = loginService;
    }

    @GetMapping("/carEquipments/equipmentslist")
    public String getAllEquipmentslist(@RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                       @RequestParam (value = "pageNumber", defaultValue = "0") int pageNumber,
                                       Model model,
                                       Authentication auth)
    {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Equipments> equipmentsList = equipmentsService.getAllCarEquipments(pageable);

        model.addAttribute("EquipmentsDto",new EquipmentsDto());
        model.addAttribute("equipmentsList",equipmentsList);
        model.addAttribute("selectedPageSize",pageSize);
        model.addAttribute("logged_email",loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));
        return "careq";
    }

    @PostMapping("/carEquipments/delete/{id}")
    public String deleteCarEquipments(@PathVariable ("id") Long id)
    {
        equipmentsService.removeCarEquipment(id);
        return "redirect:/carEquipments/equipmentslist";
    }

    @PostMapping("/carEquipments/add")
    public String addCarEquipment(@ModelAttribute @Valid EquipmentsDto equipmentsDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes)
    {
            if (bindingResult.hasErrors()){
                redirectAttributes.addFlashAttribute("error", "Incorrect characters!");
            }
            else if (equipmentsService.addCarEquipment(equipmentsDto)) {
               redirectAttributes.addFlashAttribute("result", "Equipment has been added.");
            }
            else{
               redirectAttributes.addFlashAttribute("warning", "Provided equipment already exists!");
            }

           return "redirect:/carEquipments/equipmentslist";
    }

    @PostMapping("/carequipments/deletefromcar&{car_id}&{equipments_id}")
    public String deleteEquipmentFromCar(@PathVariable ("car_id") Long car_id,
                                         @PathVariable ("equipments_id") Long equipments_id,
                                         RedirectAttributes redirectAttributes)


    {
        equipmentsService.removeEquipmentFromCar(car_id,equipments_id);
        redirectAttributes.addFlashAttribute("result","Equipment has been deleted!");
        return "redirect:/cars/edit/"+car_id;
    }


    @PostMapping("/carequipments/addtocar")
    public String addEquipmentFromCar(@RequestParam (defaultValue = "-1") Long equipments_id,
                                      @RequestParam Long car_id)
    {
        if (equipments_id == -1) {
             return "redirect:/cars/edit/"+car_id;
        }

        equipmentsService.addEquipmentFromCar(car_id,equipments_id);
        return "redirect:/cars/edit/"+car_id;
    }
}

package net.atos.controller;

import net.atos.model.*;
import net.atos.model.dto.*;
import net.atos.model.enums.Sorting;
import net.atos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
public class CarController{

    private CarService carService;
    private CarPictureService carPictureService;
    private EquipmentsService equipmentsService;
    private OrderService orderService;
    private CommentService commentService;
    private LoginService loginService;
   // private List<String> imagesWithErrors = new ArrayList<>();

    @Autowired
    public CarController(CarService carService, CarPictureService carPictureService, EquipmentsService equipmentsService, OrderService orderService, CommentService commentService, LoginService loginService) {
        this.carService = carService;
        this.carPictureService = carPictureService;
        this.equipmentsService = equipmentsService;
        this.orderService = orderService;
        this.commentService = commentService;
        this.loginService = loginService;
    }

    @GetMapping("/addcar")
    public String showForm(Model model, Authentication auth) {


        List <Equipments> carEquipmentsList = equipmentsService.getAll();
        CarDto carDto = new CarDto();

        model.addAttribute("carDto",carDto);
        model.addAttribute("carEquipmentsListSelect",carEquipmentsList);
        model.addAttribute("logged_email", loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));

        return "addCar";
    }

    @PostMapping("/addcar")
    public String addNeCar(@ModelAttribute @Valid CarDto carDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Authentication auth,
                           Model model ) throws IOException {

        List <Equipments> carEquipmentsList = equipmentsService.getAll();
        model.addAttribute("carEquipmentsListSelect",carEquipmentsList);

        // sprawdzenie roku produkcji
        if (carDto.getYear() > LocalDate.now().getYear())
        {

            model.addAttribute("yearInFuture","Year of production is in the future!");
            model.addAttribute("logged_email", loginService.getLoginFromCredentials(auth));
            model.addAttribute("isAdmin",loginService.isAdmin(auth));
            return "addCar";
        }

        // sprawdzenie pol fomularza
        if (bindingResult.hasErrors())
        {
            model.addAttribute("logged_email", loginService.getLoginFromCredentials(auth));
            model.addAttribute("isAdmin",loginService.isAdmin(auth));
            return "addCar";
        }

        List <String> stringList = carService.addNewCar(carDto); // dodajemy pojazd

        if (stringList.isEmpty())
        {
            redirectAttributes.addFlashAttribute("result","Car has been successfully added.");
        }
        else
        {
            redirectAttributes.addFlashAttribute("warningList",stringList);
        }

        return "redirect:/addcar";
    }

    @GetMapping("/add/newcar/")
    public String resultOfNewCar(@ModelAttribute("imagesWithErrors")  List<String> imagesWithErrors,
                                 Model model)
    {
        model.addAttribute("imagesWithErrors",imagesWithErrors);
        return "resultOfAddCar";
    }

    @GetMapping("/allcars")
    public String getAllCars(
            @ModelAttribute CarSearch carSearch,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam (value = "pageNumber", defaultValue = "0") int pageNumber,
            Authentication auth,
            Model model)
    {

       List<Car> carList = carService.getAllCars();

        List <String> uniqueMarks = carList.stream().map(x->x.getMark().toLowerCase()).distinct().collect(Collectors.toList());
        List <String> uniqueModels = carList.stream().map(x->x.getModel().toLowerCase()).distinct().collect(Collectors.toList());

        Sort sort;
        if (carSearch.getSorting().split("__")[1].equalsIgnoreCase("ASC")) {
            sort = new Sort(ASC, carSearch.getSorting().split("__")[0].toLowerCase());
        } else {
            sort = new Sort(DESC, carSearch.getSorting().split("__")[0].toLowerCase());
        }

        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);

        Page<Car> filteredCars = carService.getFilteredCars(carSearch.getMark(), carSearch.getModel(),
                                                            Optional.ofNullable(carSearch.getYearFrom()).orElse(0),
                                                            Optional.ofNullable(carSearch.getYearTo()).orElse(LocalDate.now().getYear()),
                                                            Optional.ofNullable(carSearch.getPriceFrom()).orElse(BigDecimal.ZERO),
                                                            Optional.ofNullable(carSearch.getPriceTo()).orElse(new BigDecimal(9999)),
                                                            pageable);

        model.addAttribute("carList", filteredCars);

        Optional<String> selectedSortingOptional = Arrays.stream(Sorting.values())
                                                          .filter(sorting -> sorting.name().equals(carSearch.getSorting()))
                                                          .map(sorting -> sorting.getDescription()).findFirst();
        String selectedSorting = carSearch.getSorting();

        if (selectedSortingOptional.isPresent())
        {
            selectedSorting = selectedSortingOptional.get();
        }

        model.addAttribute("selectedSorting",selectedSorting);
        model.addAttribute("selectedPageSize",pageSize);
        model.addAttribute("uniqueMarks",uniqueMarks);
        model.addAttribute("uniqueModels",uniqueModels);
        model.addAttribute("logged_email", loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));
        return "carList";
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/detailsofcar/{id}")
    public String carDetails(@PathVariable ("id") Long id,
                             Authentication auth,
                             Model model)
    {
        Car car = carService.getCarById(id);
           //pokazanie rezerwacji dla pojazdu
        List <Order> resevationSlots = orderService.findAllReservation(id);

        model.addAttribute("resevationSlots",resevationSlots);
        model.addAttribute("car",car);
        model.addAttribute("comments",commentService.getCommentsAssignedToCar(car)); // poprawic
        model.addAttribute("orderDto", new OrderDto());
        model.addAttribute("messageDto", new CommentDto());
        model.addAttribute("logged_email",loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));
        return "detailsOfCar";
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable ("id") Long id,
                            RedirectAttributes redirectAttributes)
    {
        carService.deleteCar(id);
        redirectAttributes.addFlashAttribute("result","Car has been removed.");
        return "redirect:/allcars";
    }


    @GetMapping("/cars/edit/{car_id}")
    public String editCar(@PathVariable ("car_id") Long car_id,
                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                          @RequestParam (value = "pageNumber", defaultValue = "0") int pageNumber,
                          Model model, Authentication auth )
    {
        Car car = carService.getCarById(car_id);
        List <Equipments> equipmentsFullList = equipmentsService.getListEquipments();
        List <Equipments> equipmentsCarList = carService.getCarById(car_id).getEquipments();

        equipmentsFullList.removeAll(equipmentsCarList);

        PagedListHolder <Equipments> carEquipments = carService.getCarEquipments(car);
        carEquipments.setPage(pageNumber);
        carEquipments.setPageSize(pageSize);

        model.addAttribute("equipmentsAllList",equipmentsFullList);
        model.addAttribute("carEquipments",carEquipments);
        model.addAttribute("carDto", car);
        model.addAttribute("car",car);
        model.addAttribute("logged_email",loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));

        return "editCar";
    }

    //edycja pol pojazdu
    @PostMapping("/cars/edit/{car_id}")
    public String editCar(@ModelAttribute @Valid CarDto carDto,
                          BindingResult bindingResult,
                          @PathVariable ("car_id") Long car_id,
                          RedirectAttributes redirectAttributes
                          ) throws IOException {
//
        if (bindingResult.hasErrors()) // bledy w formularzu do zrobienia
        {
            redirectAttributes.addFlashAttribute("result","Error in field "+bindingResult.getFieldError().getField()+"! Please correct.");
            return "redirect:/cars/edit/"+car_id;
        }

        Car car = carService.getCarById(car_id);

        //sprawdzamy limit zdjec, limit to 6 zdjec
        if (!carDto.getImageFile().isEmpty() && car.getPictures().size() == 6 )
        {
            redirectAttributes.addFlashAttribute("result","Images limit is 6.");
            return "redirect:/cars/edit/"+car_id;
        }

        boolean result = carService.updateCar(car_id,carDto);

        if (result) // jesli ok
        {
            redirectAttributes.addFlashAttribute("result","Data has been updated");
        }
        else // jest error z blednym zdjeciem
        {
            redirectAttributes.addFlashAttribute("result","Date has been updated but provided image is not in wrong format");
        }

        return "redirect:/cars/edit/"+car_id;
    }



    @GetMapping("/cars/hotoffers")
    public String getHotOffers(@RequestParam(value = "pageSize", defaultValue = "3") int pageSize,
                               @RequestParam (value = "pageNumber", defaultValue = "0") int pageNumber,
                               Model model, Authentication auth) {

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page <Car> carHotOffersPage = carService.getHotOffers(pageable);

        model.addAttribute("carHotOffersList",carHotOffersPage);
        model.addAttribute("logged_email",loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));

        return "hotOffers";
    }

}
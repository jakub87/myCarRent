package net.atos.controller;

import net.atos.service.LoginService;
import net.atos.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
public class StatisticsController {

    private StatisticsService statisticsService;
    private LoginService loginService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, LoginService loginService) {
        this.statisticsService = statisticsService;
        this.loginService = loginService;
    }

    @GetMapping("/statistics")
    public String getStatistics(@RequestParam (value = "month", defaultValue = "0") int month,
                                @RequestParam (value = "year", defaultValue = "0")  int year,
                                Authentication auth,
                                RedirectAttributes redirectAttributes,
                                Model model)  {

       //Zliczam zysk za wybrany miesiac w roku --->
        if ( year != 0 && month != 0 )
        {
            LocalDate selectedDate = LocalDate.of(year,month,1);
            LocalDate dateNow = LocalDate.now();

            if ( selectedDate.isAfter(dateNow) ) {
                redirectAttributes.addFlashAttribute("futureDate","futureDate");
                return "redirect:/statistics";

            }

            model.addAttribute("month",LocalDate.of(year,month,1).getMonth());
            model.addAttribute("year",year);

            model.addAttribute("countCars",statisticsService.countCars());
            model.addAttribute("carsStatus",statisticsService.carsStatus());
            model.addAttribute("countUsers",statisticsService.countUsers());
            model.addAttribute("usersStatus",statisticsService.usersStatus());
            model.addAttribute("profit",statisticsService.profitFromMonth(year,month)); // zysk na dany miesiac
            model.addAttribute("ordersNumbers",statisticsService.ordersInMonth(year,month)); // ile zlozonych zamowien
            model.addAttribute("ordersStatus",statisticsService.orderStatus(year,month)); // ile zakonczonych/anulowanych zamowien
            model.addAttribute("userCost",statisticsService.userDetails(year,month)); // ile hajsu wydal kazdy uzytkownik
            model.addAttribute("carStatics",statisticsService.carStatistics(year,month)); // ile razy dany pojazd zostal wypozyczony

        }

        List <Integer> yearList = new ArrayList<>();
        for (int i = statisticsService.getFirstOrder().getYear() ; i <= LocalDate.now().getYear() ; i++) {
            yearList.add(i);
        }
        model.addAttribute("yearList",yearList);
        model.addAttribute("logged_email", loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));

        return "statistics";

    }

}

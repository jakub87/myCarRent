package net.atos.service;

import net.atos.model.Car;
import net.atos.model.Order;
import net.atos.model.User;
import net.atos.model.enums.OrderStatus;
import net.atos.model.enums.Status;
import net.atos.repository.CarRepository;
import net.atos.repository.OrderRepository;
import net.atos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private OrderRepository orderRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;

    @Autowired
    public StatisticsService(OrderRepository orderRepository, CarRepository carRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public LocalDate getFirstOrder ()
    {
        return orderRepository.findFirstOrder();
    }

    public BigDecimal profitFromMonth (int year, int month)  { // zlicza zysk z miesiaca //ok

        BigDecimal profitPerMonth = orderRepository.findAllFinishedOrders()
                .stream()
                .filter(o -> (o.getOrderStatus().equals(OrderStatus.COMPLETED)
                                &&
                             o.getReturnDate().getMonthValue() == month
                                &&
                             o.getReturnDate().getYear() == year )
                                ||
                                (
                                o.getOrderStatus().equals(OrderStatus.CANCELLED)
                                  &&
                                o.getLastEditDate().getYear() == year
                                    &&
                                    o.getLastEditDate().getMonthValue() == month
                                )
                )

                .map(Order::getCost)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return profitPerMonth;
    }


    public long ordersInMonth (int year, int month) {//ok

        //ile zlozych zamowien w danym miesiacu
        long countCreationOrdersInMonth = orderRepository.findAll().stream()
                .filter(o -> o.getCreationDate().getYear() == year)
                .filter(o -> o.getCreationDate().getMonthValue() == month)
                .count();

        return countCreationOrdersInMonth;
    }


    public Map <OrderStatus,List<Order>> orderStatus(int year, int month)  { //ok

        Map<OrderStatus, List<Order>> groupByOrders = orderRepository.findAllFinishedOrders()
                .stream()
                .filter(o -> (o.getOrderStatus().equals(OrderStatus.COMPLETED)
                        &&
                        o.getReturnDate().getMonthValue() == month
                        &&
                        o.getReturnDate().getYear() == year )
                        ||
                        (
                                o.getOrderStatus().equals(OrderStatus.CANCELLED)
                                        &&
                                        o.getLastEditDate().getYear() == year
                                        &&
                                        o.getLastEditDate().getMonthValue() == month
                        )
                )
                .collect(Collectors.groupingBy(Order::getOrderStatus));

        return groupByOrders;
    }

    public Map <Car, List<Order>> carStatistics(int year, int month)  {

        Map<Car, List<Order>> cars = orderRepository.findAll()
                .stream()
                .filter(o -> o.getCreationDate().getMonthValue() == month
                        &&
                        o.getCreationDate().getYear() == year
                )
                .collect(Collectors.groupingBy(Order::getCar));

        return cars;
    }

    
    public Map<User,BigDecimal> userDetails(int year,int month) {

        Map<User, BigDecimal> userStatistics = orderRepository.findAll()
                .stream()
                .filter(o -> o.getCreationDate().getMonthValue() == month
                        &&
                        o.getCreationDate().getYear() == year)
                .collect(Collectors.groupingBy(Order::getUser, Collectors.reducing(BigDecimal.ZERO,
                        Order::getCost,
                        BigDecimal::add)));

        return userStatistics;
    }

    public Long countCars()
    {
        return carRepository.count();
    }


    public Map<Status,Long> carsStatus() {

        Map<Status, Long> carStatus = carRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Car::getStatus, Collectors.counting()));
        return carStatus;
    }

    public Long countUsers()
    {
        return userRepository.count();
    }

    public Map<Boolean, Long> usersStatus() {

        Map<Boolean, Long> userStatus = userRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(User::isStatus, Collectors.counting()));

        return userStatus;
    }

}

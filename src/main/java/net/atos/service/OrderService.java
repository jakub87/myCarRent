package net.atos.service;

import net.atos.model.Car;
import net.atos.model.Order;
import net.atos.model.dto.OrderDto;
import net.atos.model.enums.OrderStatus;
import net.atos.model.enums.Status;
import net.atos.repository.OrderRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private CarService carService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CarService carService) {
        this.orderRepository = orderRepository;
        this.carService = carService;
    }

    public Order addNewOrder(OrderDto orderDto) {
        Order order = new Order(orderDto.getCar(), orderDto.getUser(), orderDto.getStartDate(), orderDto.getEndDate(), orderDto.getCost());
        //jesli start_order rowna sie dzisiaj to zmien status pojazdu na rented

        if (order.getStartDate().equals(LocalDate.now())) {
            carService.updateCarStatus(order.getCar().getCar_id(),Status.RENTED);
        }

        return orderRepository.save(order);

    }

    public boolean checkAvailabilityOfCar(Long id, LocalDate startDate, LocalDate endDate) {
        List <Order> isFree = orderRepository.checkAvailabilityOfCar(id,startDate,endDate);

        if (isFree.isEmpty()) {
             return true;
        } else {
             return false;
        }
    }


    public void finishOrder() {
        //jesli order jest ACTIVE i juz to zamowienie sie skonczylo (enddate < now date)
        // to zmien status na Completed
        // i zmien status pojazdu na available
        orderRepository.findAll().stream()
                                 .filter(order ->
                                                  LocalDate.now().isAfter(order.getReturnDate())
                                                  &&
                                                  order.getOrderStatus().equals(OrderStatus.ACTIVE)
                                                  )
                                 .forEach(order ->
                                                  {
                                                    updateOrderStatus(order.getOrder_id(),OrderStatus.COMPLETED);
                                                    carService.updateCarStatus(order.getCar().getCar_id(),Status.AVAILABLE);
                                                  });
    }

    public void startOrder() {
       // przypadek w przyszlosci ,jesli start_order_date jest data dzisiejsza to zmien status pojazdu na rented
        orderRepository.findAll().stream()
                                 .filter(order ->
                                         order.getStartDate().isEqual(LocalDate.now())
                                         &&
                                         order.getOrderStatus().equals(OrderStatus.ACTIVE)
                                         &&
                                         order.getCar().getStatus().equals(Status.AVAILABLE)
                                         )
                                 .forEach(order ->carService.updateCarStatus(order.getCar().getCar_id(),Status.RENTED));
    }



    public void updateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = orderRepository.getOne(id);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

    public List <Order> findAllReservation(Long id) {

        return orderRepository.findAllReservationAssignedToCar(id);
    }


    public PagedListHolder <Order> getAllReservation(String valueToFind) {
        List <Order> orderList = orderRepository.findAllByUser_FirstNameContainsOrUser_LastNameContainsOrUser_EmailContainsOrCar_MarkContainsOrCar_ModelContains(valueToFind, valueToFind, valueToFind, valueToFind, valueToFind)
                .stream().sorted((o1, o2) -> o2.getReturnDate().compareTo(o1.getReturnDate()))
                .collect(Collectors.toList());
        PagedListHolder<Order> orderPage = new PagedListHolder<>(orderList);
        return orderPage;
    }

    public Order updateOrder(Long order_id) {
      LocalDate dateNow = LocalDate.now();
      Order order = orderRepository.getOne(order_id);
      LocalDate startDate = order.getStartDate();

        int daysToStartRent = Period.between(dateNow,startDate).getDays();
        BigDecimal newPrice;

        if (daysToStartRent == 1) {
            newPrice = order.getCost().multiply(BigDecimal.valueOf(0.2));
        } else if (daysToStartRent == 2) {
            newPrice = order.getCost().multiply(BigDecimal.valueOf(0.15));
        } else if (daysToStartRent == 3) {
            newPrice = order.getCost().multiply(BigDecimal.valueOf(0.1));
        } else {
            newPrice = BigDecimal.ZERO;
        }

        order.setLastEditDate(dateNow);
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setCost(newPrice);
        Car car = carService.getCarById(order.getCar().getCar_id());
        carService.updateCarStatus(car.getCar_id(), Status.AVAILABLE);
        orderRepository.save(order);

        return new Order();
    }

}

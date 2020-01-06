package net.atos.repository;


import net.atos.model.Car;
import net.atos.model.Order;
import net.atos.model.User;
import net.atos.model.dto.OrderDto;
import net.atos.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value = "SELECT * FROM orders WHERE car_id=:id AND return_date >= :startDate AND start_date <= :endDate AND order_status ='ACTIVE'", nativeQuery = true)
    List <Order> checkAvailabilityOfCar(Long id, LocalDate startDate, LocalDate endDate);

    @Query(value ="SELECT * FROM orders WHERE car_id=:id AND order_status = 'ACTIVE' ORDER BY return_date ASC", nativeQuery = true)
    List <Order> findAllReservationAssignedToCar(Long id);

    List <Order> findAllByUser_FirstNameContainsOrUser_LastNameContainsOrUser_EmailContainsOrCar_MarkContainsOrCar_ModelContains(String firstName, String lastName, String email, String brand, String model);

    @Query(value = "SELECT * FROM orders WHERE order_status != 'ACTIVE'", nativeQuery = true)
    List <Order> findAllFinishedOrders();

    @Query(value = "SELECT creation_date FROM orders ORDER BY creation_date ASC LIMIT 1",nativeQuery = true)
    LocalDate findFirstOrder();

}

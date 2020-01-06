package net.atos.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.atos.model.enums.OrderStatus;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name="Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private LocalDate startDate;

    private LocalDate returnDate;

    private LocalDate creationDate = LocalDate.now();
    private LocalDate lastEditDate = LocalDate.now();

    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ACTIVE;

    public Order(Car car, User user, LocalDate startDate, LocalDate returnDate, BigDecimal cost) {
        this.car = car;
        this.user = user;
        this.startDate = startDate;
        this.returnDate = returnDate;
        this.cost = cost;
    }
}


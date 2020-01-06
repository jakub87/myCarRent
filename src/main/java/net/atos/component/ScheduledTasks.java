package net.atos.component;

import net.atos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ScheduledTasks {

    private OrderService orderService;

    @Autowired
    public ScheduledTasks(OrderService orderService) {
         this.orderService = orderService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @PostConstruct
    public void runScheduler() {
        orderService.finishOrder();
        orderService.startOrder();

    }
}

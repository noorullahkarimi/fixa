package com.example.demo.service;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.InvalidStatusTransitionException;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatusHistory;
import com.example.demo.repository.OrderStatusHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Service
public class OrderStatusService {

    private final OrderStatusHistoryRepository historyRepository;

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS =
            new EnumMap<>(OrderStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(OrderStatus.FINAL_ORDER,
                EnumSet.of(OrderStatus.TECHNICIAN_ACCEPTED,
                        OrderStatus.CLIENT_CANCELLED,
                        OrderStatus.ADMIN_CANCELLED));

        ALLOWED_TRANSITIONS.put(OrderStatus.TECHNICIAN_ACCEPTED,
                EnumSet.of(OrderStatus.ORDER_IN_PROGRESS));

        ALLOWED_TRANSITIONS.put(OrderStatus.ORDER_IN_PROGRESS,
                EnumSet.of(OrderStatus.ORDER_COMPLETED));


        ALLOWED_TRANSITIONS.put(OrderStatus.ORDER_COMPLETED, EnumSet.noneOf(OrderStatus.class));
        ALLOWED_TRANSITIONS.put(OrderStatus.CLIENT_CANCELLED, EnumSet.noneOf(OrderStatus.class));
        ALLOWED_TRANSITIONS.put(OrderStatus.ADMIN_CANCELLED, EnumSet.noneOf(OrderStatus.class));
    }

    public OrderStatusService(OrderStatusHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }


    @Transactional
    public void changeStatus(Order order, OrderStatus newStatus, String comment) {
        OrderStatus currentStatus = order.getStatus();

        if (!isTransitionAllowed(currentStatus, newStatus)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(currentStatus);
        history.setToStatus(newStatus);
        history.setComment(comment);
        historyRepository.save(history);

        order.setStatus(newStatus);
    }


    @Transactional
    public void recordInitialStatus(Order order) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(null);
        history.setToStatus(OrderStatus.FINAL_ORDER);
        history.setComment("Order created");
        historyRepository.save(history);
    }

    private boolean isTransitionAllowed(OrderStatus from, OrderStatus to) {
        Set<OrderStatus> allowed = ALLOWED_TRANSITIONS.get(from);
        return allowed != null && allowed.contains(to);
    }
}

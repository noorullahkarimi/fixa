package com.example.demo.model;


import com.example.demo.enums.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "order_status_histories")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 30)
    private OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 30)
    private OrderStatus toStatus;

    @Column(name = "changed_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime changedAt;

    @Column(length = 500)
    private String comment;

}

package dev.cong.v.springcomereme.entity;

import dev.cong.v.springcomereme.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "shipping_fee")
    private Double shippingFee;

    @Column(name = "total")
    private Double total;

    @PrePersist
    public void prePersist() {
        this.status = OrderStatus.Pending;
        this.createdAt = new Date(System.currentTimeMillis());
    }

    public void addDetails(OrderDetails orderDetail){
        if (this.orderDetails==null){
            this.orderDetails = new ArrayList<>();
        }
        this.orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }
}

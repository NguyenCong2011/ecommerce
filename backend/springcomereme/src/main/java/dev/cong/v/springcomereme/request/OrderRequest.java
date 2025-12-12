package dev.cong.v.springcomereme.request;

import dev.cong.v.springcomereme.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Long addressId;

    private Double total;

    private List<OrderDetailsRequest> orderDetails;

    private Double shippingFee;

}

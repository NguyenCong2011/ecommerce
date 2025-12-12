package dev.cong.v.springcomereme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.cong.v.springcomereme.entity.Order;
import dev.cong.v.springcomereme.enums.OrderStatus;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    private Long id;
    private Date createdAt;
    private List<OrderDetailsDTO> orderDetails;
    private UserDTO user;
    private OrderStatus status;
    private AddressDTO address;
    private Double shippingFee;
    private Double total;

    public static OrderDTO toDTO(Order order){
        return  OrderDTO.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .user(UserDTO.toDTO(order.getUser()))
                .status(order.getStatus())
                .address(AddressDTO.toDTO(order.getAddress()))
                .shippingFee(order.getShippingFee())
                .total(order.getTotal())
                .orderDetails(order.getOrderDetails().stream().map(OrderDetailsDTO::toDTO).toList())
                .build();

    }




}

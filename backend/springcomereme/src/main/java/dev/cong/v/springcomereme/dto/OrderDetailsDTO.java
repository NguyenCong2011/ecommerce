package dev.cong.v.springcomereme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.cong.v.springcomereme.entity.OrderDetails;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailsDTO {

    private Long id;

    private ProductItemDTO productItem;

    private Integer quantity;

    public static OrderDetailsDTO toDTO(OrderDetails orderDetails){
        return  OrderDetailsDTO.builder()
                .id(orderDetails.getId())
                .productItem(ProductItemDTO.toDTOWithParentsInfos(orderDetails.getProductItem()))
                .quantity(orderDetails.getQuantity())
                .build();
    }


}

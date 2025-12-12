package dev.cong.v.springcomereme.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ProductItemRequest {

    private  Long id;
    private  Long  quantity;
    private Long productSizeId;



}

package dev.cong.v.springcomereme.dto;

import dev.cong.v.springcomereme.entity.ProductColor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductColorDTO {
    private  Long id;
    private String value;

    public  static  ProductColorDTO toDTO(ProductColor productColor){
        return  ProductColorDTO.builder()
                .id(productColor.getId())
                .value(productColor.getValue())
                .build();
    }
}

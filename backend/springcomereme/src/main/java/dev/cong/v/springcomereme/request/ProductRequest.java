package dev.cong.v.springcomereme.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequest  {

    private String name;
    private String description;
    private String image;
    private Long categoryId;
    private Long brandId;
    private Double price;
    private Long sizeId;

}

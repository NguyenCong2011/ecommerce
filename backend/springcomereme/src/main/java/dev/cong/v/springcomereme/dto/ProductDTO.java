package dev.cong.v.springcomereme.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import dev.cong.v.springcomereme.entity.Category;
import dev.cong.v.springcomereme.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String avatar;
    private List<String> images;
    private Long categoryId;
    private Long brandId;
    private Double price;
    private Long productColorId;
    private List<ProductItemDTO> productItems;
    private Category category;
    private BrandDTO brand;
    private String colorName;
    private List<String> sizes;
    private Boolean showHomepage;

    public  static  ProductDTO toDTO(Product product){

        return  ProductDTO.builder()
                .name(product.getName())
                .id(product.getId())
                .description(product.getDescription())
                .avatar(product.getAvatar())
                .images(product.getImages())
                .category(product.getCategory())
                .price(product.getPrice())
                .brand(BrandDTO.toDTO(product.getBrand()))
                .showHomepage(product.getShowHomepage())
                .productColorId(product.getProductColor().getId())
                .colorName(product.getProductColor().getValue())
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
                .build();

    }

    public  static  ProductDTO toDTODisplayScreen(Product product){

        ProductDTO productDTO = ProductDTO.toDTO(product);


        List<String> sizes = product.getProductItems().stream()
                .map(item -> item.getProductSize().getValue())
                .distinct()
                .toList();

        productDTO.setSizes(sizes);

        return productDTO;

    }

    public  static  ProductDTO toDTOWithItems (Product product){
        ProductDTO productDTO = ProductDTO.toDTODisplayScreen(product);
        List<ProductItemDTO> productItemDTOS = product.getProductItems()
                .stream().map(ProductItemDTO::toDTO).toList();

        productDTO.setProductItems(productItemDTOS);

        return  productDTO;
    }
}

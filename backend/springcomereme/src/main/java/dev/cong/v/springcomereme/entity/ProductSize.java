package dev.cong.v.springcomereme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "product_sizes")
@Builder
public class ProductSize {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "value",unique = true)
    private String value;

    @OneToMany(mappedBy = "productSize",cascade = CascadeType.ALL)
    private List<ProductItem> productItems;

    public  void addProductItem(ProductItem productItem){
        if(this.productItems == null){
            this.productItems =  new ArrayList<>();
        }
        this.productItems.add(productItem);
        productItem.setProductSize(this);
    }

    public ProductSize(Long id, String value) {
        this.id = id;
        this.value = value;
    }
}

package dev.cong.v.springcomereme.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_colors")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductColor {

    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private  Long id;

    @Column(name = "value",unique = true)
    private String value;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST}
            , fetch = FetchType.LAZY,mappedBy = "productColor")
    private List<Product> products;

    public   void addProduct(Product product){
        if(this.products == null){
            this.products = new ArrayList<>();
        }
        this.products.add(product);
        product.setProductColor(this);
    }

    public ProductColor(String value) {
        this.value = value;
    }

    public ProductColor(Long id, String value) {
        this.id = id;
        this.value = value;
    }
}

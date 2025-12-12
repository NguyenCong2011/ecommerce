package dev.cong.v.springcomereme.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "product_category")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "image")
    private  String image;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category",fetch = FetchType.LAZY)
    List<Product> products ;




    public void addProduct(Product product){
        if(this.products == null){
            this.products = new ArrayList<>();
        }
        this.products.add(product);
        product.setCategory(this);
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

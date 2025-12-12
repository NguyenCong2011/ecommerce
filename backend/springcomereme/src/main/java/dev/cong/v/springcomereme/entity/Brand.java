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
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade ={CascadeType.MERGE,
            CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH},
            mappedBy = "brand"
    )
    private List<Product> products;

    public  void addProduct(Product product){
        if (this.products == null){
            this.products= new ArrayList<>();
        }
        this.products.add(product);
        product.setBrand(this);
    }

    public Brand(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

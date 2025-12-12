package dev.cong.v.springcomereme.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description",length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "avatar")
    private  String avatar;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_image",joinColumns = @JoinColumn(name = "product_id"))
    private  List<String> images;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private  Category category;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product")
    private List<ProductItem> productItems;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "price")
    private Double price;

    @Column(name = "show_home_page")
   private Boolean showHomepage;

    public void addProductItem(ProductItem productItem){
        if(this.productItems == null){
            this.productItems= new ArrayList<>();
        }
        this.productItems.add(productItem);
        productItem.setProduct(this);

    }

    @ManyToOne
    @JoinColumn(name = "color_id")
    private ProductColor productColor;


    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date(System.currentTimeMillis());
        this.showHomepage = false;
    }



}

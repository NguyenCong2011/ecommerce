package dev.cong.v.springcomereme.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "product_item")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "quantity")
    private  Long  quantity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private  Product product;


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "size_id")
    private   ProductSize productSize;

    @OneToMany(mappedBy = "productItem",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ShoppingCartItem> shoppingCartItems;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "productItem",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderDetails> orderDetails;


    @PrePersist
    public void prePersist() {
        this.createdAt = new Date(System.currentTimeMillis());
    }

}

package dev.cong.v.springcomereme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "shopping_cart")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShoppingCart {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.PERSIST,CascadeType.REFRESH}
        , mappedBy = "shoppingCart"
    )
    public User user;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "shoppingCart")
    private List<ShoppingCartItem> cartItems;

    @Column(name = "created_at")
    private Date createdAt;

    public  void addCartItem(ShoppingCartItem item){
        if(this.cartItems == null){
            this.cartItems = new ArrayList<>();
        }
        this.cartItems.add(item);
        item.setShoppingCart(this);
    }

    @PrePersist
    public  void pre(){
        this.createdAt = new java.util.Date(System.currentTimeMillis());
    }


}

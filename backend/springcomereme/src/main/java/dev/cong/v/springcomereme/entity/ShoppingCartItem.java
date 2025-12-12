package dev.cong.v.springcomereme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "shopping_cart_item")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;


    @ManyToOne
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private ProductItem productItem;

    public ShoppingCartItem(ShoppingCart shoppingCart, Long quantity, ProductItem productItem) {
        this.shoppingCart = shoppingCart;
        this.quantity = quantity;
        this.productItem = productItem;
    }
}

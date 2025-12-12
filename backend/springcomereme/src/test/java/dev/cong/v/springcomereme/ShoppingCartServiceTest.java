package dev.cong.v.springcomereme;



import dev.cong.v.springcomereme.dao.ProductItemRepository;
import dev.cong.v.springcomereme.dao.ShoppingCartItemRepository;
import dev.cong.v.springcomereme.dao.ShoppingCartRepository;
import dev.cong.v.springcomereme.dao.UserRepository;
import dev.cong.v.springcomereme.entity.*;
import dev.cong.v.springcomereme.dao.*;

import dev.cong.v.springcomereme.entity.*;
import dev.cong.v.springcomereme.request.CartRequest;

import dev.cong.v.springcomereme.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartItemRepository shoppingCartItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductItemRepository productItemRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private User user;
    private ShoppingCart shoppingCart;
    private ProductItem productItem;
    private ShoppingCartItem shoppingCartItem;
    private CartRequest cartRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("user@example.com");

        shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        user.setShoppingCart(shoppingCart);

        productItem = new ProductItem();
        productItem.setId(1L);

        ProductSize productSize = new ProductSize();

        productItem.setProductSize(productSize);

        Product product = new Product();
        product.setName("Gadget");
        product.setPrice(299.99);
        product.setAvatar("image_url");
        ProductColor productColor = new ProductColor();
        productColor.setValue("Red");
        product.setProductColor(productColor);

        product.addProductItem(productItem);

        productItem.setProduct(product);

        shoppingCartItem = new ShoppingCartItem(shoppingCart, 1L, productItem);

        cartRequest = new CartRequest();
        cartRequest.setProductItemId(1L);
        cartRequest.setQuantity(2L);
    }

    @Test
    void createShoppingCartItem_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(productItemRepository.findById(anyLong())).thenReturn(Optional.of(productItem));
        when(shoppingCartItemRepository.findByShoppingCartIdAndProductItemId(1L, 1L))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = shoppingCartService.create(cartRequest, "user@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }

    @Test
    void getAuthShoppingCart_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        user.getShoppingCart().addCartItem(shoppingCartItem);

        ResponseEntity<?> response = shoppingCartService.getAuth("user@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void deleteShoppingCartItem_Success() {
        when(shoppingCartItemRepository.findById(anyLong())).thenReturn(Optional.of(shoppingCartItem));

        ResponseEntity<?> response = shoppingCartService.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(shoppingCartItemRepository).delete(shoppingCartItem);
    }

    @Test
    void updateShoppingCartItemQuantity_Success() {
        when(shoppingCartItemRepository.findById(anyLong())).thenReturn(Optional.of(shoppingCartItem));

        ResponseEntity<?> response = shoppingCartService.updateQuantity(1L, cartRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Updated");
        verify(shoppingCartItemRepository).save(shoppingCartItem);
        assertThat(shoppingCartItem.getQuantity()).isEqualTo(cartRequest.getQuantity());
    }

}

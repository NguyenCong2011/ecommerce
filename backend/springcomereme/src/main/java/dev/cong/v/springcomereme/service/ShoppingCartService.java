package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.ProductItemRepository;
import dev.cong.v.springcomereme.dao.ShoppingCartItemRepository;
import dev.cong.v.springcomereme.dao.ShoppingCartRepository;
import dev.cong.v.springcomereme.dao.UserRepository;
import dev.cong.v.springcomereme.dto.CartItemDTO;
import dev.cong.v.springcomereme.entity.ShoppingCart;
import dev.cong.v.springcomereme.entity.ShoppingCartItem;
import dev.cong.v.springcomereme.request.CartRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShoppingCartService {
    private  final ShoppingCartRepository shoppingCartRepository;
    private  final ProductItemRepository productItemRepository;
    private  final UserRepository userRepository;
    private  final ShoppingCartItemRepository shoppingCartItemRepository;

    public ResponseEntity<?> create(CartRequest cartRequest, String email) {

        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Not Found User")
        );

        var productItem = productItemRepository.findById(cartRequest.getProductItemId())
                .orElseThrow(
                        ()-> new EntityNotFoundException("Not Found Product")
                );

        ShoppingCart shoppingCart = user.getShoppingCart();
        if(shoppingCart == null){
            shoppingCart =  new ShoppingCart();
            shoppingCart.setUser(user);
            user.setShoppingCart(shoppingCart);

        }
        var existingItemOpt = shoppingCartItemRepository.findByShoppingCartIdAndProductItemId(
                shoppingCart.getId(), productItem.getId());

        if (existingItemOpt.isPresent()) {
            ShoppingCartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartRequest.getQuantity());
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem(shoppingCart, cartRequest.getQuantity(), productItem);
            shoppingCart.addCartItem(newItem);
        }

        shoppingCartRepository.save(shoppingCart);

        return ResponseEntity.status(HttpStatus.CREATED).body("Ok");

    }

    public ResponseEntity<?> getAuth(String username) {
        var user  = userRepository.findByEmail(username)
                .orElseThrow(()-> new EntityNotFoundException("Not Found User"));

       ShoppingCart shoppingCart =  user.getShoppingCart();
        if(shoppingCart==null){
            throw  new EntityNotFoundException("Cart is empty");
        }
        List<CartItemDTO> cartItemList = shoppingCart.getCartItems()
                .stream().map(CartItemDTO::toDTO).toList();

        return  ResponseEntity.ok(cartItemList);

    }

    public ResponseEntity<?> delete(Long id) {
        var cart = shoppingCartItemRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found cart " +id)
        );

        shoppingCartItemRepository.delete(cart);

        return  ResponseEntity.ok("Deleted");

    }

    public ResponseEntity<?> updateQuantity(Long id, CartRequest cartRequest) {
        var cart = shoppingCartItemRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found cart " +id)
        );

        cart.setQuantity(cartRequest.getQuantity());

        shoppingCartItemRepository.save(cart);

        return  ResponseEntity.ok("Updated");
    }
}

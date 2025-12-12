package dev.cong.v.springcomereme;

import dev.cong.v.springcomereme.dao.AddressRepository;
import dev.cong.v.springcomereme.dao.OrderRepository;
import dev.cong.v.springcomereme.dao.ProductItemRepository;
import dev.cong.v.springcomereme.dao.UserRepository;
import dev.cong.v.springcomereme.entity.*;
import dev.huynh.v.springcomereme.dao.*;
import dev.cong.v.springcomereme.dto.OrderDTO;
import dev.huynh.v.springcomereme.entity.*;
import dev.cong.v.springcomereme.request.OrderDetailsRequest;
import dev.cong.v.springcomereme.request.OrderRequest;
import dev.cong.v.springcomereme.service.OrderService;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ProductItemRepository productItemRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private OrderRequest orderRequest;
    private ProductItem productItem;

    @BeforeEach
    void setup() {
        user = new User();
        user.setEmail("test@example.com");

        orderRequest = new OrderRequest();
        orderRequest.setAddressId(1L);
        orderRequest.setTotal(100.0);
        orderRequest.setShippingFee(10.0);
        orderRequest.setOrderDetails(new ArrayList<>());

        productItem = new ProductItem();
        productItem.setId(36L);

        ProductSize productSize = new ProductSize();
        productSize.setValue("48");
        productItem.setProductSize(productSize);
        productItem.setQuantity(10L);

        Product product = new Product();
        product.setId(36L);

        ProductColor productColor = new ProductColor();
        productColor.setValue("Red");
        product.setProductColor(productColor);

        product.addProductItem(productItem);

        Order order = new Order();
    }

    @Test
    void createOrder_UsesExistingProductItem() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(addressRepository.findById(anyLong())).willReturn(Optional.of(new Address()));
        given(productItemRepository.findById(36L)).willReturn(Optional.of(productItem));

        OrderDetailsRequest details = new OrderDetailsRequest();
        details.setProductItemId(36L);
        details.setQuantity(5);

        List<OrderDetailsRequest> detailsList = new ArrayList<>();
        detailsList.add(details);
        orderRequest.setOrderDetails(detailsList);

        ResponseEntity<?> response = orderService.create(orderRequest, "test@example.com");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isInstanceOf(OrderDTO.class);
    }



    @Test
    void createOrder_UserNotFound_ThrowsEntityNotFoundException() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(orderRequest, "test@example.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Not Found User");
    }
}

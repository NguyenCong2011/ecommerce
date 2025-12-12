package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.AddressRepository;
import dev.cong.v.springcomereme.dao.OrderRepository;
import dev.cong.v.springcomereme.dao.ProductItemRepository;
import dev.cong.v.springcomereme.dao.UserRepository;
import dev.cong.v.springcomereme.dto.OrderDTO;
import dev.cong.v.springcomereme.entity.Order;
import dev.cong.v.springcomereme.entity.OrderDetails;
import dev.cong.v.springcomereme.request.OrderDetailsRequest;
import dev.cong.v.springcomereme.request.OrderRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrderService {
    private  final OrderRepository repository;
    private  final AddressRepository addressRepository;
    private  final UserRepository userRepository;
    private final ProductItemRepository productItemRepository;

    public ResponseEntity<?> create(OrderRequest request, String username) {

        var user = userRepository.findByEmail(username).orElseThrow(
                ()-> new EntityNotFoundException("Not Found User")
        );

        var address= addressRepository.findById(request.getAddressId()).orElseThrow(
                ()-> new EntityNotFoundException("Not Found Address")
        );

        Order order = Order.builder()
                .user(user)
                .address(address)
                .total(request.getTotal())
                .shippingFee(request.getShippingFee())
                .build();
        if(request.getOrderDetails().isEmpty()){
            throw  new NoSuchElementException("No Order Details");
        }
       addOrderDetails(order,request.getOrderDetails());

        repository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(OrderDTO.toDTO(order));


    }
    private void addOrderDetails(Order order, List<OrderDetailsRequest> detailsRequests) {
        for (OrderDetailsRequest detailsRequest : detailsRequests) {
            var productItem = productItemRepository.findById(detailsRequest.getProductItemId()).orElseThrow(
                    () -> new EntityNotFoundException("Product Item with ID '" + detailsRequest.getProductItemId() + "' not found.")



            );

            productItem.setQuantity(productItem.getQuantity() - detailsRequest.getQuantity());
            productItemRepository.save(productItem);
            OrderDetails orderDetails = OrderDetails.builder()
                    .quantity(detailsRequest.getQuantity())
                    .productItem(productItem)
                    .build();

            order.addDetails(orderDetails);
        }
    }

    public ResponseEntity<?> getByAuth(String username) {
        var user = userRepository.findByEmail(username).orElseThrow(
                ()-> new EntityNotFoundException("Not Found User")
        );
        List<OrderDTO> orderDTOS = user.getOrders().stream().map(OrderDTO::toDTO).toList();

        return  ResponseEntity.ok(orderDTOS);
    }

    public ResponseEntity<?> getById(Long id) {

        var order =repository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found Order : "+ id)
        );

        return ResponseEntity.ok(OrderDTO.toDTO(order));

    }

    public ResponseEntity<?> getAll() {
        List<OrderDTO> orderDTOS = repository.findAll().stream().map(OrderDTO::toDTO).toList();

        return  ResponseEntity.ok(orderDTOS);
    }

    public ResponseEntity<?> delete(Long id) {
        var order =repository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found Order : "+ id)
        );

        repository.delete(order);
        return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
    }

    public ResponseEntity<?> changeStatus(Long id, OrderRequest orderDTO) {
        System.out.println(orderDTO);
        var order =repository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found Order : "+ id)
        );


        order.setStatus(orderDTO.getStatus());
        repository.save(order);
        return ResponseEntity.ok("Changed");

    }
}

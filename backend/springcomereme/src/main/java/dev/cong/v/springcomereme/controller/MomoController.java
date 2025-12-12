package dev.cong.v.springcomereme.controller;

import dev.cong.v.springcomereme.dao.OrderRepository;
import dev.cong.v.springcomereme.entity.Order;
import dev.cong.v.springcomereme.enums.OrderStatus;
import dev.cong.v.springcomereme.request.MomoIPNRequest;
import dev.cong.v.springcomereme.response.CreateMomoResponse;
import dev.cong.v.springcomereme.service.MomoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/momo")
public class MomoController {
    private final MomoService momoService;
    private final OrderRepository orderRepository;


    @PostMapping("/create/{orderId}")
    public CreateMomoResponse createQR(@PathVariable Long orderId) throws Exception {
        return momoService.createQR(orderId);
    }

    @PostMapping("/ipn-handler")
    public ResponseEntity<?> handleIPN(@RequestBody MomoIPNRequest request) {

        log.info("=== MOMO IPN CALLBACK ===");
        log.info("OrderId: {}", request.getOrderId());
        log.info("Amount: {}", request.getAmount());
        log.info("ResultCode: {}", request.getResultCode());
        log.info("Message: {}", request.getMessage());

        try {
            Long orderId = Long.parseLong(request.getOrderId());

            Order order = orderRepository.findById(orderId).orElse(null);

            if (order == null) {
                log.error("Order not found: {}", orderId);
                return ResponseEntity.ok("{\"message\":\"Order not found\"}");
            }

            if (request.getResultCode() == 0) {
                order.setStatus(OrderStatus.Confirmed);
                log.info("Payment success for order {}", orderId);
            } else {
                order.setStatus(OrderStatus.Cancelled);
                log.info("Payment failed for order {}", orderId);
            }

            orderRepository.save(order);

        } catch (Exception e) {
            log.error("IPN Error: {}", e.getMessage());
        }

        return ResponseEntity.ok("{\"message\":\"Success\"}");
    }
}
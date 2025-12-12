package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.MomoApi;
import dev.cong.v.springcomereme.dao.OrderRepository;
import dev.cong.v.springcomereme.entity.Order;
import dev.cong.v.springcomereme.request.CreateMomoRequest;
import dev.cong.v.springcomereme.response.CreateMomoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MomoService {

    @Value("${DEV_PARTNER_CODE}")
    private String PARTNER_CODE;

    @Value("${DEV_ACCESS_KEY}")
    private String ACCESS_KEY;

    @Value("${DEV_SECRET_KEY}")
    private String SECRET_KEY;

    @Value("${return-url}")
    private String REDIRECT_URL;

    @Value("${ipn-url}")
    private String IPN_URL;

    private final MomoApi momoApi;

    private final OrderRepository orderRepository; // Thêm repository

    private static final String REQUEST_TYPE = "captureWallet";

    public CreateMomoResponse createQR(Long orderId) throws Exception {
        // 1. Tìm order trong database
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // 2. Lấy amount từ order
        long amount = order.getTotal().longValue();

        String orderIdStr = order.getId().toString();
        String requestId = UUID.randomUUID().toString();
        String extraData = "orderId=" + orderId;

        // 3. Tạo orderInfo từ thông tin order
        String orderInfo = String.format("Thanh toán đơn hàng #%s",
                orderId);

        // 4. Tạo signature
        String rawSignature =
                "accessKey=" + ACCESS_KEY +
                        "&amount=" + amount +
                        "&extraData=" + extraData +
                        "&ipnUrl=" + IPN_URL +
                        "&orderId=" + orderIdStr +
                        "&orderInfo=" + orderInfo +
                        "&partnerCode=" + PARTNER_CODE +
                        "&redirectUrl=" + REDIRECT_URL +
                        "&requestId=" + requestId +
                        "&requestType=" + REQUEST_TYPE;

        String signature = signHmacSHA256(rawSignature, SECRET_KEY);

        // 5. Tạo request
        CreateMomoRequest request = CreateMomoRequest.builder()
                .partnerCode(PARTNER_CODE)
                .requestType(REQUEST_TYPE)
                .ipnUrl(IPN_URL)
                .redirectUrl(REDIRECT_URL)
                .orderId(orderIdStr)
                .orderInfo(orderInfo)
                .amount(amount)
                .requestId(requestId)
                .extraData(extraData)
                .signature(signature)
                .lang("vi")
                .build();

        log.info("Creating MoMo payment for order: {}, amount: {}", orderId, amount);

        return momoApi.createPayment(request);
    }
    private String signHmacSHA256(String data, String key) throws Exception {
        String algorithm = "HmacSHA256";
        Mac hmacSHA256 = Mac.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

package dev.cong.v.springcomereme.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMomoResponse {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private long amount;
    private int resultCode;
    private String message;
    private String payUrl;
    private String deeplink;
    private String qrCodeUrl;
}

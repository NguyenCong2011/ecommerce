package dev.cong.v.springcomereme.request;

import lombok.Data;


@Data
public class OrderDetailsRequest {

    private Long productItemId;

    private Integer quantity;

}

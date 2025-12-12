package dev.cong.v.springcomereme.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    Pending,
    Confirmed,
    Shipped,
    Delivered,
    Cancelled
}

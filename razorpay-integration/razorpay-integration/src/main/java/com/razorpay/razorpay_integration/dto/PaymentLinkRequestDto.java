package com.razorpay.razorpay_integration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentLinkRequestDto {
    private String orderId;
    private String customerName;
    private String phone;
    private int amount;
}

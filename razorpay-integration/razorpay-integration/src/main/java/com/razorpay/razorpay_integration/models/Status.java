package com.razorpay.razorpay_integration.models;

import lombok.Data;

import java.util.Map;
@Data
public class Status {
    private PaymentStatus paymentStatus;
    private Map<Object,Object> paymentMap;
}

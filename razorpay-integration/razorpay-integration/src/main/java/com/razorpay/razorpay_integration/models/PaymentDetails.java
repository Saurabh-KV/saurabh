package com.razorpay.razorpay_integration.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class PaymentDetails extends BaseModel{
    private String orderId;
    private String paymentId;
    private String PaymentLink;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Date date;
}

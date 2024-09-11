package com.razorpay.razorpay_integration.service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.razorpay_integration.dto.PaymentLinkRequestDto;
import com.razorpay.razorpay_integration.models.PaymentDetails;
import com.razorpay.razorpay_integration.models.PaymentResponse;
import com.razorpay.razorpay_integration.models.PaymentStatus;
import com.razorpay.razorpay_integration.models.Status;
import com.razorpay.razorpay_integration.repository.PaymentRepository;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Data
public class RazorPayGateway implements PaymentGateway{

    private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepository;
    @Override
    public String createPaymentLink(PaymentLinkRequestDto paymentLinkRequestDto) {

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",paymentLinkRequestDto.getAmount());
        paymentLinkRequest.put("currency","INR");
        paymentLinkRequest.put("expire_by", LocalDate.now().plusDays(7).atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
        paymentLinkRequest.put("reference_id",paymentLinkRequestDto.getOrderId());
        paymentLinkRequest.put("description","Payment for order no " + paymentLinkRequestDto.getOrderId());

        JSONObject customer = new JSONObject();
        customer.put("name",paymentLinkRequestDto.getCustomerName());
        customer.put("contact",paymentLinkRequestDto.getPhone());
        customer.put("email","saurabhuike@gmail.com");
        paymentLinkRequest.put("customer",customer);

        JSONObject notes = new JSONObject();
        notes.put("demo payment","100");
        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","https://www.kognivera.com");
        paymentLinkRequest.put("callback_method","get");

        try {
            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
            return payment.get("short_url");
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create payment link", e);
        }
    }

    @Override
    public PaymentStatus getStatus(String paymentId, String orderId) {
        Map<Object,Object> map=new HashMap<>();
        try {
            Payment payment = razorpayClient.payments.fetch(paymentId);
            String statusType = payment.get("status");
            JSONObject json = payment.toJson();
            map.put("paymentId",json.get("id"));
            map.put("orderId",json.get("order_id"));
            map.put("email",json.get("email"));
            map.put("method",json.get("method"));
            System.out.println(map);
            return switch (statusType) {
                case "captured" -> PaymentStatus.SUCCESS;
                case "failed" -> PaymentStatus.FAILURE;
                default -> PaymentStatus.INITIATED;
            };
        } catch (RazorpayException e) {
            throw new RuntimeException("Unable to fetch the payment details", e);
        }
    }

    public PaymentDetails getPaymentInfo(String paymentId) {
        PaymentDetails paymentDetails = paymentRepository.findByPaymentId(paymentId).orElseThrow(() -> new RuntimeException());
        return paymentDetails;
    }

    public Status getStatuss(String paymentId) {
        Map<Object,Object> map=new HashMap<>();
        try {
            Payment payment = razorpayClient.payments.fetch(paymentId);
            String statusType = payment.get("status");
            JSONObject json = payment.toJson();
            map.put("paymentId",json.get("id"));
            map.put("orderId",json.get("order_id"));
            map.put("email",json.get("email"));
            map.put("method",json.get("method"));
            Status status=new Status();
            status.setPaymentMap(map);
            switch (statusType) {
                case "captured" -> status.setPaymentStatus(PaymentStatus.SUCCESS);
                case "failed" -> status.setPaymentStatus(PaymentStatus.FAILURE);
                default -> status.setPaymentStatus(PaymentStatus.INITIATED);
            };
            return status;
        } catch (RazorpayException e) {
            throw new RuntimeException("Unable to fetch the payment details", e);
        }
    }
}

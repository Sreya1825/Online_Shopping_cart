package com.shopcart.orderservice.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paymentWindow")
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Card Number is mandatory")
    private String cardNumber;

    @NotBlank(message = "CVV is mandatory")
    private String cvv;

    @NotNull(message = "Amount is mandatory")
    private float amount; // Fetched from cartValue

    @NotBlank(message = "PaymentMethod is mandatory")
    @Pattern(regexp = "^(CREDIT_CARD|DEBIT_CARD|PAYPAL|BANK_TRANSFER|CASH_ON_DELIVERY)$",
            message = "Payment method must be one of: CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER, CASH_ON_DELIVERY")
    private String paymentMethod;
}
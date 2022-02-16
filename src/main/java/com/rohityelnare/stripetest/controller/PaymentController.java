package com.rohityelnare.stripetest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rohityelnare.stripetest.dto.CreatePayment;
import com.rohityelnare.stripetest.dto.CreatePaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
public class PaymentController {
	
	@Value("${stripe.api.key}")
	String stripekey;
	
	@PostMapping("/create-payment-intent")
	public CreatePaymentResponse createPaymentIntent(@RequestBody CreatePayment createPayment) throws StripeException {
		Stripe.apiKey = stripekey;	
		PaymentIntentCreateParams createParams =
	        new PaymentIntentCreateParams.Builder()
	        	.setCurrency("inr")
	        	.setAmount(11*100L)
	        	.build();

	      PaymentIntent paymentIntent = PaymentIntent.create(createParams);
	      return new CreatePaymentResponse(paymentIntent.getClientSecret());
	}
}

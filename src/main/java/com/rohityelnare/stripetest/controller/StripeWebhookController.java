package com.rohityelnare.stripetest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;

@RestController
public class StripeWebhookController {
	
	@Value("${stripe.webhook.secret}")
	private String endpointSecret;

	@PostMapping("/stripe/events")
	public String handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
		String endpointSecret = "whsec_...";
			
			if(sigHeader == null)
				return "";
			
			Event event;
                try {
                    event = Webhook.constructEvent(
                        payload, sigHeader, endpointSecret
                    );
                } catch (SignatureVerificationException e) {
                    // Invalid signature
                    System.out.println("⚠️  Webhook error while validating signature.");
//                    response.status(400);
                    return "";
                }
            // Deserialize the nested object inside the event
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {
                // Deserialization failed, probably due to an API version mismatch.
            }
            // Handle the event
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
                    break;
                case "payment_method.attached":
                    PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                    break;
                default:
                    System.out.println("Unhandled event type: " + event.getType());
                break;
            }
            return "";
	}
}

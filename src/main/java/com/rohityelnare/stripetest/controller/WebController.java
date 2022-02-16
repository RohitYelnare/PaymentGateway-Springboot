package com.rohityelnare.stripetest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.stripe.Stripe;

public class WebController {
	
	@Value("${stripe.api.key}")
	String stripeApiKey;
	
	@Value("${stripe.public.key}")
	private String stripePublicKey;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("stripePublicKey", stripePublicKey);
		Stripe.apiKey = stripeApiKey;
		return "index";
	}
	
}

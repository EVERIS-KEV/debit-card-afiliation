package com.everis.debitcardafiliation.consumer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.everis.debitcardafiliation.constant.Constants;

public class Webclient {

	public static final org.springframework.web.reactive.function.client.WebClient customer = org.springframework.web.reactive.function.client.WebClient
			.create(Constants.Path.CUSTOMERS_PATH);

	public static final org.springframework.web.reactive.function.client.WebClient logic = org.springframework.web.reactive.function.client.WebClient
			.create(Constants.Path.LOGIC_PATH);

	public static final org.springframework.web.reactive.function.client.WebClient creditAccount = org.springframework.web.reactive.function.client.WebClient
			.create(Constants.Path.CREDIT_ACCOUNT_PATH);

	public static final org.springframework.web.reactive.function.client.WebClient currentAccount = org.springframework.web.reactive.function.client.WebClient
			.builder().baseUrl(Constants.Path.CURRENT_ACCOUNT_PATH)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

	public static final org.springframework.web.reactive.function.client.WebClient savingAccount = org.springframework.web.reactive.function.client.WebClient
			.builder().baseUrl(Constants.Path.SAVING_ACCOUNT_PATH)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

	public static final org.springframework.web.reactive.function.client.WebClient fixedAccount = org.springframework.web.reactive.function.client.WebClient
			.builder().baseUrl(Constants.Path.FIXED_ACCOUNT_PATH)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
}
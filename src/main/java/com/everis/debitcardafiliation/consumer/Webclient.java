package com.everis.debitcardafiliation.consumer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.client.WebClient;

import com.everis.debitcardafiliation.constant.Constants;

public class Webclient {

	public static final WebClient customer = WebClient.create(Constants.Path.CUSTOMERS_PATH);

	public static final WebClient logic = WebClient.create(Constants.Path.LOGIC_PATH);

	public static final WebClient creditAccount = WebClient.create(Constants.Path.CREDIT_ACCOUNT_PATH);

	public static final WebClient currentAccount = WebClient.builder().baseUrl(Constants.Path.CURRENT_ACCOUNT_PATH)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

	public static final WebClient savingAccount = WebClient.builder().baseUrl(Constants.Path.SAVING_ACCOUNT_PATH)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

	public static final WebClient fixedAccount = WebClient.builder().baseUrl(Constants.Path.FIXED_ACCOUNT_PATH)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
}
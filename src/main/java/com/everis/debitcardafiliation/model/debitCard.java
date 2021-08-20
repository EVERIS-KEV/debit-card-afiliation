package com.everis.debitcardafiliation.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.everis.debitcardafiliation.consumer.webclient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "debit-card")
public class debitCard {
	@Id
	private String idDebitCard;

	@NotBlank(message = "El campo cliente no debe estar vacio.")
	private String idCustomer;

	@Size(min = 7, message = "El campo password de tener mas de 7 carácteres como máximo.")
	@NotBlank(message = "El campo password no debe estar vacio.")
	private String password;

	private Date dateCreated = new Date();
	private String accountNumber = webclient.logic.get().uri("/generatedNumberLong/10").retrieve()
			.bodyToMono(String.class).block();

	private List<account> accounts = new ArrayList<account>();

	public debitCard(String idCustomer, String password) {
		this.idCustomer = idCustomer;
		this.password = password;
	}

}

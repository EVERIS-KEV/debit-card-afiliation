package com.everis.debitcardafiliation.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.everis.debitcardafiliation.consumer.Webclient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovementFrom {
	@NotBlank(message = "Debe ingresar su número de cuenta.")
	private String numberDebitCard;
	@NotBlank(message = "Debe ingresar su contraseña.")
	private String password;

	private Date date = new Date();
	private String numberAccount;

	@NotBlank(message = "Debe ingresar un monto.")
	private Double amount;

	public MovementFrom(String numberDebitCard, String password, String numberAccount, Double amount) {
		this.numberDebitCard = numberDebitCard;
		this.password = Webclient.logic.get().uri("/encriptBySha1/" + password).retrieve().bodyToMono(String.class)
				.block();
		this.numberAccount = numberAccount;
		this.amount = amount;
	}

}

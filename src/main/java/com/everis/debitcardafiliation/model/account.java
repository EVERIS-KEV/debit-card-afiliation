package com.everis.debitcardafiliation.model;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class account {

	@NotBlank(message = "El campo numero de cuenta no debe estar vacio.")
	private String numberAccount;
	private Boolean principal;

	public account() {
		this.principal = false;
	}

	public account(String numberAccount) {
		this.numberAccount = numberAccount;
		this.principal = false;
	}

	public account(String numberAccount, Boolean principal) {
		this.numberAccount = numberAccount;
		this.principal = principal;
	}
}

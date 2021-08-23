package com.everis.debitcardafiliation.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountAffiliate {

	@NotBlank(message = "El campo numero de cuenta no debe estar vacio.")
	private String numberAccount;
	private Boolean principal;
	private Date dateAffiliate = new Date();

	public AccountAffiliate() {
		this.principal = false;
	}

	public AccountAffiliate(String numberAccount) {
		this.numberAccount = numberAccount;
		this.principal = false;
	}

	public AccountAffiliate(String numberAccount, Boolean principal) {
		this.numberAccount = numberAccount;
		this.principal = principal;
	}
}

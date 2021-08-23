package com.everis.debitcardafiliation.map;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class movementsMapper {
	private Date dateCreated;
	private String type;
	private double amount;
	private String accountEmisor;
	private String accountRecep;

	public movementsMapper(String accountEmisor, double amount) {
		this.dateCreated = new Date();
		this.accountEmisor = accountEmisor;
		this.amount = amount;
		this.type = "Retiro";
	}
}

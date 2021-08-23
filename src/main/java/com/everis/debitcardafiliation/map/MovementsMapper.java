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
public class MovementsMapper {
	private Date dateCreated;
	private String type;
	private double amount;
	private String accountEmisor;
	private String accountRecep;

	public MovementsMapper(String accountEmisor, double amount) {
		this.dateCreated = new Date();
		this.accountEmisor = accountEmisor;
		this.amount = amount;
		this.type = "Retiro";
	}
}

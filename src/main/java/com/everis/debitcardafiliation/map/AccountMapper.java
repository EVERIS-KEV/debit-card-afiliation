package com.everis.debitcardafiliation.map;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountMapper {
	private String profile;
	private String accountNumber;
	private double amount;
	private Date dateCreated;
	private String typeAccount;
	private List<MovementsMapper> movement;
	private String idCustomer;
}

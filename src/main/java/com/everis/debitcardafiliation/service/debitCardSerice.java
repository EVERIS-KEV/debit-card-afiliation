package com.everis.debitcardafiliation.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.everis.debitcardafiliation.consumer.webclient;
import com.everis.debitcardafiliation.dto.message;
import com.everis.debitcardafiliation.map.accountData;
import com.everis.debitcardafiliation.model.account;
import com.everis.debitcardafiliation.model.debitCard;
import com.everis.debitcardafiliation.repository.debitCardRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class debitCardSerice {
	@Autowired
	debitCardRepository repository;

	private Boolean verifyCustomer(String id) {
		return webclient.customer.get().uri("/verifyId/{id}", id).retrieve().bodyToMono(Boolean.class).block();
	}

	private Boolean verifyNumberCC(String number) {
		return webclient.currentAccount.get().uri("/verifyByNumberAccount/" + number).retrieve()
				.bodyToMono(Boolean.class).block();
	}

	private Boolean verifyNumberSC(String number) {
		return webclient.savingAccount.get().uri("/verifyByNumberAccount/" + number).retrieve()
				.bodyToMono(Boolean.class).block();
	}

	private Boolean verifyNumberFC(String number) {
		return webclient.fixedAccount.get().uri("/verifyByNumberAccount/" + number).retrieve().bodyToMono(Boolean.class)
				.block();
	}

	private Boolean verifyAccount(String number) {
		if (verifyNumberCC(number) || verifyNumberSC(number) || verifyNumberFC(number))
			return true;
		return false;
	}

	private Boolean findAccountNumber(String id, String number) {
		return !repository.findById(id).get().getAccounts().stream().filter(c -> c.getNumberAccount().equals(number))
				.collect(Collectors.toList()).isEmpty();
	}

	private accountData findCurrentAccountByNumber(String number) {
		return webclient.currentAccount.get().uri("/byNumberAccount/{number}", number).retrieve()
				.bodyToMono(accountData.class).block();
	}

	private accountData findSavingAccountByNumber(String number) {
		return webclient.savingAccount.get().uri("/byNumberAccount/{number}", number).retrieve()
				.bodyToMono(accountData.class).block();
	}

	private accountData findFixedAccountByNumber(String number) {
		return webclient.fixedAccount.get().uri("/byNumberAccount/{number}", number).retrieve()
				.bodyToMono(accountData.class).block();
	}

	private String customerByAccount(String number) {
		if (verifyNumberSC(number))
			return findSavingAccountByNumber(number).getIdCustomer();

		if (verifyNumberCC(number))
			return findCurrentAccountByNumber(number).getIdCustomer();

		else
			return findFixedAccountByNumber(number).getIdCustomer();
	}

	private Boolean verifyDebitCard(String id, account model) {
		try {
			if (repository.findAll().stream().filter(c -> c.getIdDebitCard().equals(id)).collect(Collectors.toList())
					.isEmpty())
				return true;

			if (!verifyAccount(model.getNumberAccount()))
				return true;

			if (!customerByAccount(model.getNumberAccount()).equals(repository.findById(id).get().getIdCustomer()))
				return true;

			return false;
		} catch (Exception e) {
			return true;
		}
	}

	public Mono<Object> save(debitCard model) {
		try {

			if (!verifyCustomer(model.getIdCustomer()))
				return Mono.just(new message("Cliente no econtrado."));

			repository.save(model);
			return Mono.just(new message("Registrado correctamente."));
		} catch (Exception e) {
			return Mono.just(new message("Datos inválidos."));
		}
	}

	public Mono<Object> setPrincipalAccount(String id, account model) {

		if (verifyDebitCard(id, model))
			return Mono.just(new message("Datos incorrectos."));

		if (!findAccountNumber(id, model.getNumberAccount()))
			return Mono.just(new message("Esta cuenta no está afiliada a su tarjeta."));

		debitCard card = repository.findById(id).get();

		card.getAccounts().stream().map(c -> {
			if (!c.getNumberAccount().equals(model.getNumberAccount()))
				c.setPrincipal(false);
			else
				c.setPrincipal(true);
			return c;
		}).collect(Collectors.toList());

		repository.save(card);

		return Mono.just(new message("Tarjeta actualizada."));
	}

	public Mono<Object> addAccount(String id, account model) {

		if (verifyDebitCard(id, model))
			return Mono.just(new message("Datos incorrectos."));

		if (findAccountNumber(id, model.getNumberAccount()))
			return Mono.just(new message("Esta cuenta ya está registrada."));

		if (repository.findById(id).get().getAccounts().size() == 0) {
			if (model.getPrincipal())
				repository.findById(id).get().getAccounts().add(model);
			else
				return Mono.just(new message("Esta es su única cuenta y debe ser la principal."));
		} else {
			if (!model.getPrincipal())
				repository.findById(id).get().getAccounts().add(model);
			else
				return Mono.just(new message("Ya tiene una cuenta principal."));
		}

		repository.save(repository.findById(id).get());
		return Mono.just(new message("Cuenta añadida."));
	}

	public Mono<Object> get(String number) {
		try {
			return Mono.just(
					repository.findAll().stream().filter(c -> c.getAccountNumber().equals(number)).findFirst().get());
		} catch (Exception e) {
			return Mono.just(new message("Tarjeta no econtrada."));
		}
	}

	public Flux<Object> getAll() {
		return Flux.fromIterable(repository.findAll());
	}

	public Flux<Object> getFindByCustomer(String id) {
		return Flux.fromIterable(
				repository.findAll().stream().filter(c -> c.getIdCustomer().equals(id)).collect(Collectors.toList()));
	}

}

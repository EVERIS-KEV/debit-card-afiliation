package com.everis.debitcardafiliation.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.everis.debitcardafiliation.consumer.webclient;
import com.everis.debitcardafiliation.dto.message;
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

	public Mono<Object> addAccount(String id, account model) {
		try {

			if (repository.findAll().stream().filter(c -> c.getIdDebitCard().equals(id)).collect(Collectors.toList())
					.isEmpty())
				return Mono.just(new message("Id incorrecto."));

			if (!verifyAccount(model.getNumberAccount()))
				return Mono.just(new message("Cuenta no registrada."));

			// repository.save(model);
			return Mono.just(new message("Registrado correctamente."));
		} catch (Exception e) {
			return Mono.just(new message("Datos inválidos."));
		}
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

package com.everis.debitcardafiliation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.everis.debitcardafiliation.consumer.webclient;
import com.everis.debitcardafiliation.dto.message;
import com.everis.debitcardafiliation.dto.movementFrom;
import com.everis.debitcardafiliation.map.accountMapper;
import com.everis.debitcardafiliation.map.movementsMapper;
import com.everis.debitcardafiliation.model.accountAffiliate;
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
		if (verifyNumberCC(number) || verifyNumberSC(number) || verifyNumberFC(number)) {
			return true;
		}
		return false;
	}

	private Boolean findAccountNumber(String id, String number) {
		return !repository.findById(id).get().getAccounts().stream().filter(c -> c.getNumberAccount().equals(number))
				.collect(Collectors.toList()).isEmpty();
	}

	private accountMapper findCurrentAccountByNumber(String number) {
		return webclient.currentAccount.get().uri("/byNumberAccount/{number}", number).retrieve()
				.bodyToMono(accountMapper.class).block();
	}

	private accountMapper findSavingAccountByNumber(String number) {
		return webclient.savingAccount.get().uri("/byNumberAccount/{number}", number).retrieve()
				.bodyToMono(accountMapper.class).block();
	}

	private accountMapper findFixedAccountByNumber(String number) {
		return webclient.fixedAccount.get().uri("/byNumberAccount/{number}", number).retrieve()
				.bodyToMono(accountMapper.class).block();
	}

	private accountMapper findByAccount(String number) {
		if (verifyNumberSC(number)) {
			return findSavingAccountByNumber(number);
		}
		if (verifyNumberCC(number)) {
			return findCurrentAccountByNumber(number);
		} else {
			return findFixedAccountByNumber(number);
		}
	}

	private Boolean verifyDebitCard(String id, accountAffiliate model) {
		try {
			if (repository.findAll().stream().filter(c -> c.getIdDebitCard().equals(id)).collect(Collectors.toList())
					.isEmpty() || !verifyAccount(model.getNumberAccount())
					|| !findByAccount(model.getNumberAccount()).getIdCustomer()
					.equals(repository.findById(id).get().getIdCustomer())) {
				return true;
			}

			return false;
		} catch (Exception e) {
			return true;
		}
	}

	private movementFrom authCard(movementFrom model) {

		if (!repository.findAll().stream().filter(c -> c.getCardNumber().equals(model.getNumberDebitCard())
				&& c.getPassword().equals(model.getPassword())).collect(Collectors.toList()).isEmpty()) {
			return model;
		}
		return null;
	}

	private Double getAllAmount(String number) {
		double amount = 0.0;
		debitCard card = repository.findAll().stream().filter(c -> c.getCardNumber().equals(number)).findFirst().get();

		/*
		 * for (int i = 0; i < card.getAccounts().size(); i++) { amount = amount +
		 * findByAccount(card.getAccounts().get(i).getNumberAccount()).getAmount(); }
		 */

		return repository.findAll().stream().filter(c -> c.getCardNumber().equals(number)).findFirst().get()
				.getAccounts().stream().mapToDouble(c -> findByAccount(c.getNumberAccount()).getAmount()).sum();
	}

	private void AddMovementAccount(String number, Double amount) {
		movementsMapper model = new movementsMapper(number, amount);
		if (verifyNumberSC(number)) {
			webclient.savingAccount.post().uri("/movememts").body(Mono.just(model), movementsMapper.class).retrieve()
			.bodyToMono(Object.class).subscribe();
		}
		if (verifyNumberCC(number)) {
			webclient.currentAccount.post().uri("/movememts").body(Mono.just(model), movementsMapper.class).retrieve()
			.bodyToMono(Object.class).subscribe();
		}
		if (verifyNumberFC(number)) {
			webclient.fixedAccount.post().uri("/movememts").body(Mono.just(model), movementsMapper.class).retrieve()
			.bodyToMono(Object.class).subscribe();
		}
	}

	public Mono<Object> save(debitCard model) {
		try {

			if (!verifyCustomer(model.getIdCustomer())) {
				return Mono.just(new message("Cliente no econtrado."));
			}

			repository.save(model);
			return Mono.just(new message("Registrado correctamente."));
		} catch (Exception e) {
			return Mono.just(new message("Datos inválidos."));
		}
	}

	public Mono<Object> setPrincipalAccount(String id, accountAffiliate model) {

		if (verifyDebitCard(id, model)) {
			return Mono.just(new message("Datos incorrectos."));
		}

		if (!findAccountNumber(id, model.getNumberAccount())) {
			return Mono.just(new message("Esta cuenta no está afiliada a su tarjeta."));
		}

		debitCard card = repository.findById(id).get();

		card.getAccounts().stream().map(c -> {
			if (!c.getNumberAccount().equals(model.getNumberAccount())) {
				c.setPrincipal(false);
			} else {
				c.setPrincipal(true);
			}
			return c;
		}).collect(Collectors.toList());

		repository.save(card);

		return Mono.just(new message("Tarjeta actualizada."));
	}

	public Mono<Object> addAccount(String id, accountAffiliate model) {

		debitCard card = repository.findById(id).get();

		if (verifyDebitCard(id, model)) {
			return Mono.just(new message("Datos incorrectos."));
		}

		if (findAccountNumber(id, model.getNumberAccount())) {
			return Mono.just(new message("Esta cuenta ya está afiliada a su tarjeta."));
		}

		if (repository.findById(id).get().getAccounts().size() == 0) {
			if (model.getPrincipal()) {
				card.getAccounts().add(model);
			} else {
				return Mono.just(new message("Esta es su única cuenta y debe ser la principal."));
			}
		} else {
			if (!model.getPrincipal()) {
				card.getAccounts().add(model);
			} else {
				return Mono.just(new message("Ya tiene una cuenta principal."));
			}
		}

		repository.save(card);
		return Mono.just(new message("Cuenta añadida."));
	}

	public Mono<Object> addMovements(movementFrom model) {
		debitCard debitcard = repository.findAll().stream()
				.filter(c -> c.getCardNumber().equals(model.getNumberDebitCard())).findFirst().get();
		String id = debitcard.getIdDebitCard();
		Double amount = model.getAmount();

		accountMapper accountPrincipal = getAccountPrincipal(id).block();

		if (authCard(model) == null) {
			return Mono.just(new message("Datos incorrectos."));
		}

		System.out.println(getAllAmount(model.getNumberDebitCard()));

		if (accountPrincipal.getAmount() < amount) {
			double amountAll = getAllAmount(model.getNumberDebitCard());

			if (amountAll < amount) {
				return Mono.just(new message("El monto sobre pasa el todas sus cuentas."));
			} else {
				amount = amount - accountPrincipal.getAmount();
				// AddMovementAccount(accountPrincipal.getAccountNumber(),
				// accountPrincipal.getAmount());

				System.out.println(amount);

				List<accountAffiliate> accountNoPrincipal = debitcard.getAccounts().stream()
						.filter(c -> !c.getPrincipal()).collect(Collectors.toList());

				for (int i = 0; i < accountNoPrincipal.size(); i++) {
					Double amountTemp = findByAccount(accountNoPrincipal.get(i).getNumberAccount()).getAmount();
					if ((amountTemp < amount) && (amountTemp != 0)) {
						amount = amount - amountTemp;
						// AddMovementAccount(accountNoPrincipal.get(i).getNumberAccount(), amountTemp);
						System.out.println(amount);
					}
					if (amountTemp > amount) {
						// AddMovementAccount(accountNoPrincipal.get(i).getNumberAccount(), amount);
						System.out.println(amount);
						break;
					}
				}

				/*
				 * accountNoPrincipal.forEach( c->{ double amountTemp =
				 * findByAccount(c.getNumberAccount()).getAmount(); double amountF =
				 * model.getAmount(); if ((amountTemp < amountF) && (amountTemp != 0)) { amountF
				 * = amountF - amountTemp; AddMovementAccount(c.getNumberAccount(), amountTemp);
				 * System.out.println(amountF); } if (amountTemp > amountF) {
				 * AddMovementAccount(c.getNumberAccount(), amountF);
				 * System.out.println(amountF); return ; } } );
				 */
			}
			return Mono.just(new message("El monto sobre pasa el de la cuenta principal, operaciones realizadas."));
		}

		// AddMovementAccount(accountPrincipal.getAccountNumber(), model.getAmount());

		return Mono.just(new message("Operación realizada."));
	}

	public Mono<Object> get(String number) {
		try {
			return Mono
					.just(repository.findAll().stream().filter(c -> c.getCardNumber().equals(number)).findAny().get());
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

	public Mono<accountMapper> getAccountPrincipal(String id) {
		return Mono.just(findByAccount(repository.findById(id).get().getAccounts().stream()
				.filter(c -> c.getPrincipal()).findAny().get().getNumberAccount()));
	}

}

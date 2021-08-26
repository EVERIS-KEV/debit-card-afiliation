package com.everis.debitcardafiliation.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*; 

import com.everis.debitcardafiliation.dto.*; 
import com.everis.debitcardafiliation.map.*;
import com.everis.debitcardafiliation.model.*; 
import com.everis.debitcardafiliation.service.DebitCardService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
@RequestMapping()
public class DebitCardController {
	@Autowired
	private DebitCardService service;

	private Mono<Object> errorResult(BindingResult bindinResult) {
		String msg = "";

		for (int i = 0; i < bindinResult.getAllErrors().size(); i++) {
			msg = bindinResult.getAllErrors().get(0).getDefaultMessage();
		}

		return Mono.just(new Message(msg));
	}

	@GetMapping("/")
	public Flux<Object> getAll() {
		return service.getAll();
	}

	@GetMapping("/byNumber/{number}")
	public Mono<Object> getByNumber(@PathVariable("number") String number) {
		return service.get(number);
	}

	@GetMapping("/findByCustomer/{id}")
	public Flux<Object> getByCustomer(@PathVariable("id") String id) {
		return service.getFindByCustomer(id);
	}

	@GetMapping("/getPrincipal/{id}")
	public Mono<AccountMapper> getPrincipal(@PathVariable("id") String id) {
		return service.getAccountPrincipal(id);
	}

	@PostMapping("/save")
	public Mono<Object> create(@RequestBody @Valid DebitCard model, BindingResult bindinResult) {

		if (bindinResult.hasErrors())
			return errorResult(bindinResult);

		return service.save(model);
	}

	@PostMapping("/addAccount/{id}")
	public Mono<Object> addAccount(@PathVariable("id") String id, @RequestBody @Valid AccountAffiliate model,
			BindingResult bindinResult) {

		if (bindinResult.hasErrors())
			return errorResult(bindinResult);

		return service.addAccount(id, model);
	}

	@PostMapping("/setPrincipal/{id}")
	public Mono<Object> setPrincipal(@PathVariable("id") String id, @RequestBody @Valid AccountAffiliate model,
			BindingResult bindinResult) {

		if (bindinResult.hasErrors())
			return errorResult(bindinResult);

		return service.setPrincipalAccount(id, model);
	}

	@PostMapping("/movements")
	public Mono<Object> addMovements(@RequestBody @Valid MovementFrom model, BindingResult bindinResult) {

		if (bindinResult.hasErrors())  return errorResult(bindinResult); 

		return Mono.just(service.addMovement(model));

	}
}

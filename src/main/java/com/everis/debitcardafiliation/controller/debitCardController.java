package com.everis.debitcardafiliation.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.everis.debitcardafiliation.dto.message;
import com.everis.debitcardafiliation.model.account;
import com.everis.debitcardafiliation.model.debitCard;
import com.everis.debitcardafiliation.service.debitCardSerice;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
@RequestMapping()
public class debitCardController {
	@Autowired
	private debitCardSerice service;

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

	@PostMapping("/save")
	public Mono<Object> create(@RequestBody @Valid debitCard model, BindingResult bindinResult) {
		String msg = "";

		if (bindinResult.hasErrors()) {
			for (int i = 0; i < bindinResult.getAllErrors().size(); i++)
				msg = bindinResult.getAllErrors().get(0).getDefaultMessage();
			return Mono.just(new message(msg));
		}

		return service.save(model);
	}

	@PostMapping("/addAccount/{id}")
	public Mono<Object> create(@PathVariable("id") String id, @RequestBody @Valid account model,
			BindingResult bindinResult) {
		String msg = "";

		if (bindinResult.hasErrors()) {
			for (int i = 0; i < bindinResult.getAllErrors().size(); i++)
				msg = bindinResult.getAllErrors().get(0).getDefaultMessage();
			return Mono.just(new message(msg));
		}

		System.out.println(id);

		return service.addAccount(id, model);
	}
}

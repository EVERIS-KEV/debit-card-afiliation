package com.everis.debitcardafiliation.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.everis.debitcardafiliation.model.debitCard;

@Repository
public interface debitCardRepository extends MongoRepository<debitCard, String> {

}

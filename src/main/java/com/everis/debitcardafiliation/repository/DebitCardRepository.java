package com.everis.debitcardafiliation.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.everis.debitcardafiliation.model.DebitCard;

@Repository
public interface DebitCardRepository extends MongoRepository<DebitCard, String> {

}

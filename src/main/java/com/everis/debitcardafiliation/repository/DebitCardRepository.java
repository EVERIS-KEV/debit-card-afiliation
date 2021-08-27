package com.everis.debitcardafiliation.repository;

import com.everis.debitcardafiliation.model.DebitCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitCardRepository extends MongoRepository<DebitCard, String> {}

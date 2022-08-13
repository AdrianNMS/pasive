package com.bank.pasive.models.dao;

import com.bank.pasive.models.documents.Pasive;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IPasiveDao extends ReactiveMongoRepository<Pasive, String> {

}

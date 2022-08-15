package com.bank.pasive.services;

import com.bank.pasive.models.documents.Pasive;
import com.bank.pasive.models.utils.Mont;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IPasiveService
{
    Mono<List<Pasive>> FindAll();
    Mono<Pasive> Find(String id);
    Mono<Pasive> Create(Pasive pasive);
    Mono<Pasive> Update(String id, Pasive pasive);
    Mono<Object> Delete(String id);
    Mono<Pasive> SetMont(String id, Mont mont);

    Mono<Mont> GetMont(String id);
}

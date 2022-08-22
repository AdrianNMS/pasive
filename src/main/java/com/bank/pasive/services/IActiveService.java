package com.bank.pasive.services;

import com.bank.pasive.models.utils.ResponseMont;
import reactor.core.publisher.Mono;

public interface IActiveService
{
    Mono<ResponseMont> getClientDebt(String idClient);
}

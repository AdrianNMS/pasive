package com.bank.pasive.services;

import com.bank.pasive.models.utils.ResponseActive;
import reactor.core.publisher.Mono;

public interface IActiveService
{
    Mono<ResponseActive> checkCreditCard(String id, Integer typeCreditCard);
}

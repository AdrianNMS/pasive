package com.bank.pasive.services;

import com.bank.pasive.models.utils.ResponseDebitCard;
import reactor.core.publisher.Mono;

public interface IDebitCardService
{
    Mono<ResponseDebitCard> getDebitCardPasives(String idDebitCard);
}

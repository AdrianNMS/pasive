package com.bank.pasive.services.impl;

import com.bank.pasive.models.utils.ResponseDebitCard;
import com.bank.pasive.services.IDebitCardService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DebitCardImpl implements IDebitCardService
{

    @Override
    public Mono<ResponseDebitCard> getDebitCardPasives(String idDebitCard) {
        return null;
    }
}

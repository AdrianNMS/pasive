package com.bank.pasive.services.impl;

import com.bank.pasive.models.utils.ResponseDebitCard;
import com.bank.pasive.models.utils.ResponseParameter;
import com.bank.pasive.services.IDebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DebitCardImpl implements IDebitCardService
{
    @Autowired
    @Qualifier("getWebClientDebitCard")
    WebClient webClient;
    @Override
    public Mono<ResponseDebitCard> getDebitCardPasives(String idDebitCard) {
        return webClient.get()
                .uri("/api/debitCard/debit/"+ idDebitCard)
                .retrieve()
                .bodyToMono(ResponseDebitCard.class);
    }
}

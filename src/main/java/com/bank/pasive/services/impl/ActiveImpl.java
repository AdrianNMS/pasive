package com.bank.pasive.services.impl;

import com.bank.pasive.models.utils.ResponseMont;
import com.bank.pasive.services.IActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ActiveImpl implements IActiveService
{
    @Autowired
    @Qualifier("getWebClientActive")
    WebClient webClient;

    @Override
    public Mono<ResponseMont> getClientDebt(String idClient) {
        return webClient.get()
                .uri("/api/active/debt/client/"+ idClient)
                .retrieve()
                .bodyToMono(ResponseMont.class);
    }
}

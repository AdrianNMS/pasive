package com.bank.pasive.services.impl;

import com.bank.pasive.models.utils.ResponseParameter;
import com.bank.pasive.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ClientImpl implements IClientService {

    @Autowired
    @Qualifier("getWebClientClient")
    WebClient webClient;

    @Override
    public Mono<ResponseParameter> getParam(String clientId, Integer code)
    {
        return webClient.get()
                .uri("/api/client/param/"+ clientId +"/"+ code)
                .retrieve()
                .bodyToMono(ResponseParameter.class);
    }
}

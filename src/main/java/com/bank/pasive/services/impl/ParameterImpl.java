package com.bank.pasive.services.impl;

import com.bank.pasive.models.utils.ResponseParameter;
import com.bank.pasive.services.IParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class ParameterImpl implements IParameterService {


    @Autowired
    @Qualifier("getWebClientParameter")
    WebClient webClient;

    @Override
    public Mono<ResponseParameter> findByCode(Integer code)
    {
        return webClient.get()
                .uri("/api/parameter/catalogue/"+ code)
                .retrieve()
                .bodyToMono(ResponseParameter.class);
    }
}

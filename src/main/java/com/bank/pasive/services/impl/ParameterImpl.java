package com.bank.pasive.services.impl;

import com.bank.pasive.models.utils.ResponseParameter;
import com.bank.pasive.services.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ParameterImpl implements ParameterService {

    @Autowired
    WebClient webClient;

    @Override
    public Mono<ResponseParameter> getAllUsers(){
        return webClient.get()
                .uri("/api/parameter/")
                .retrieve()
                .bodyToMono(ResponseParameter.class)
                ;

    }
}

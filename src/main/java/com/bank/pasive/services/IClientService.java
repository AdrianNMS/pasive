package com.bank.pasive.services;

import com.bank.pasive.models.utils.ResponseParameter;
import reactor.core.publisher.Mono;

public interface IClientService {

    Mono<ResponseParameter> getParam(String clientId, Integer code);
}

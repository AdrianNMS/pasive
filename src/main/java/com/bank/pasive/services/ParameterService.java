package com.bank.pasive.services;

import com.bank.pasive.models.utils.ResponseParameter;
import reactor.core.publisher.Mono;

public interface ParameterService {

    Mono<ResponseParameter> getAllUsers();
}

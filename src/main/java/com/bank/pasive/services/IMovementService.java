package com.bank.pasive.services;

import com.bank.pasive.models.utils.ResponseMovement;
import reactor.core.publisher.Mono;

public interface IMovementService
{
    Mono<ResponseMovement> getBalance(String id);
}

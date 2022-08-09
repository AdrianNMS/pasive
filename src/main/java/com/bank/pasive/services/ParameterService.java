package com.bank.pasive.services;

import com.bank.pasive.models.documents.Parameter;
import com.bank.pasive.models.utils.ResponseParameter;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ParameterService {

    Mono<ResponseParameter> getAllUsers();

    List<Parameter> getParameter(List<Parameter> listParameter, Integer code);
}

package com.bank.pasive.controllers.helpers;

import com.bank.pasive.handler.ResponseHandler;
import com.bank.pasive.models.documents.Parameter;
import com.bank.pasive.models.documents.Pasive;
import com.bank.pasive.services.IClientService;
import com.bank.pasive.services.IPasiveService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class FindTypeHelper
{
    public static Mono<ResponseEntity<Object>> getParameterService(Logger log, IClientService clientService, Pasive pasive)
    {
        return clientService.getParam(pasive.getClientId(),pasive.getPasivesType().getValue())
                .doOnNext(responseParameter -> log.info(responseParameter.toString()))
                .flatMap(responseParameter ->
                {
                    if(responseParameter.getData() != null)
                    {
                        Parameter parameter = responseParameter.getData();

                        if(parameter.getTransactionDay().equals("true"))
                        {
                            LocalDateTime localDateTime = pasive.getSpecificDay().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate().atStartOfDay();
                            parameter.setTransactionDay(localDateTime.getDayOfMonth()+"");
                        }

                        return Mono.just(ResponseHandler.response("Done", HttpStatus.OK, responseParameter.getData()));
                    }
                    else
                    {
                        return Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null));
                    }
                });
    }

    public static Mono<ResponseEntity<Object>> FindPasive(Logger log, IClientService parameterService, IPasiveService pasiveService, String id)
    {
        return pasiveService.Find(id)
            .doOnNext(pasive -> log.info(pasive.toString()))
            .flatMap(pasive ->
                    getParameterService(log,parameterService,pasive)
            )
            .doFinally(fin -> log.info("[END] Find Type Pasive"));
    }

    public static Mono<ResponseEntity<Object>> FindTypeSequence(Logger log, IClientService parameterService, IPasiveService pasiveService, String id)
    {
        return FindPasive(log,parameterService,pasiveService,id);
    }
}

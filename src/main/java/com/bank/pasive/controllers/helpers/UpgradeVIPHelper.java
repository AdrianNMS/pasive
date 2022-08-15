package com.bank.pasive.controllers.helpers;

import com.bank.pasive.handler.ResponseHandler;
import com.bank.pasive.models.documents.Pasive;
import com.bank.pasive.services.IActiveService;
import com.bank.pasive.services.IMovementService;
import com.bank.pasive.services.IPasiveService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static com.bank.pasive.models.enums.PasiveNameType.SAVINGVIP;

public class UpgradeVIPHelper
{
    public static Mono<ResponseEntity<Object>> UpdatePasive(Logger log, IPasiveService pasiveService, Pasive p)
    {
        log.info("Final");
        p.setPasivesType(SAVINGVIP);
        return pasiveService.Update(p.getId(),p)
                .flatMap(pasive -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, pasive)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] Create Pasive"));
    }

    public static Mono<ResponseEntity<Object>> CheckBalance(Logger log, IPasiveService pasiveService, IMovementService movementService, Pasive p)
    {

        return movementService.getBalance(p.getId()).flatMap(responseMovement -> {
            log.info(responseMovement.toString());
            if(responseMovement.getData()>0)
                return UpdatePasive(log,pasiveService,p);
            else
                return Mono.just(ResponseHandler.response("You don't have enough balance in your credit card", HttpStatus.OK, null));
        })
        .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
        .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)));
    }

    public static Mono<ResponseEntity<Object>> CheckCreditCard(Logger log, IPasiveService pasiveService, IMovementService movementService, IActiveService activeService, Pasive p)
    {
        return activeService.checkCreditCard(p.getClientId(), 2002).flatMap(responseActive ->
                {
                    log.info(p.getClientId());
                    log.info(responseActive.toString());
                    if(responseActive.getData())
                        return CheckBalance(log,pasiveService,movementService,p);
                    else
                        return Mono.just(ResponseHandler.response("Not Found", HttpStatus.NO_CONTENT, null));
                }

        )
        .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
        .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)));
    }

    public static Mono<ResponseEntity<Object>> FindActive(Logger log, IPasiveService pasiveService, IMovementService movementService, IActiveService activeService, String id)
    {
        return pasiveService.Find(id)
                .flatMap(pasive -> {
                    if(pasive!=null)
                    {
                        return CheckCreditCard(log,pasiveService,movementService,activeService,pasive);
                    }
                    else
                        return Mono.just(ResponseHandler.response("Not Found", HttpStatus.NO_CONTENT, null));
                })
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)));
    }

    public static Mono<ResponseEntity<Object>> UpdateVIPSequence(Logger log, IPasiveService pasiveService, IMovementService movementService, IActiveService activeService, String id)
    {
        return FindActive(log,pasiveService,movementService,activeService,id);
    }


}

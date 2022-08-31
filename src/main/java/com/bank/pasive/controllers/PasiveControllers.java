package com.bank.pasive.controllers;

import com.bank.pasive.controllers.helpers.FindTypeHelper;
import com.bank.pasive.handler.ResponseHandler;
import com.bank.pasive.models.documents.Pasive;
import com.bank.pasive.models.utils.DebitCardPasive;
import com.bank.pasive.models.utils.Mont;
import com.bank.pasive.services.IActiveService;
import com.bank.pasive.services.IClientService;
import com.bank.pasive.services.IDebitCardService;
import com.bank.pasive.services.IPasiveService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/pasive")
public class PasiveControllers {

    @Autowired
    private IPasiveService pasiveService;

    @Autowired
    private IClientService parameterService;

    @Autowired
    private IDebitCardService debitCardService;

    @Autowired
    private IActiveService activeService;
    private static final Logger log = LoggerFactory.getLogger(PasiveControllers.class);
    private static final String RESILENCE_SERVICE = "defaultConfig";

    @PostMapping
    public Mono<ResponseEntity<Object>> Create(@Valid @RequestBody Pasive p) {
        log.info("[INI] Create Pasive");
        p.setCreatedDate(LocalDateTime.now());

        return activeService.getClientDebt(p.getClientId())
                .flatMap(responseMont -> {
                    if(responseMont.getData() !=null)
                    {
                        log.info(responseMont.toString());

                        if(responseMont.getData().getMont()<=0)
                            return pasiveService.Create(p)
                                    .flatMap(pasive -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, pasive)));
                        else
                            return Mono.just(ResponseHandler.response("You have debts", HttpStatus.BAD_REQUEST, null));

                    }
                    else
                        return Mono.just(ResponseHandler.response("Not found", HttpStatus.NOT_FOUND, null));
                })
                .doFinally(fin -> log.info("[END] Create Pasive"));

    }

    @PostMapping("/mont/{id}")
    public Mono<ResponseEntity<Object>> setMontData(@PathVariable String id, @RequestBody Mont m) {
        log.info("[INI] setMont Pasive");
        return pasiveService.SetMont(id,m)
                .flatMap(pasive -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, pasive)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] setMont Pasive"));
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> FindAll() {
        log.info("[INI] FindAll Pasive");

        return pasiveService.FindAll()
                .flatMap(pasives -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, pasives)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] FindAll Pasive"));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> Find(@PathVariable String id) {
        log.info("[INI] Find Pasive");
        return pasiveService.Find(id)
                .flatMap(pasive -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, pasive)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] Find Pasive"));
    }

    @GetMapping("/mont/{id}")
    public Mono<ResponseEntity<Object>> getMontData(@PathVariable String id) {
        log.info("[INI] getMont Data");
        return pasiveService.GetMont(id)
                .flatMap(mont -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, mont)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] getMont Data"));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> Update(@PathVariable("id") String id,@Valid @RequestBody Pasive p) {
        log.info("[INI] Update Pasive");
        p.setUpdateDate(LocalDateTime.now());
        return pasiveService.Update(id,p)
                .flatMap(pasive -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, pasive)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] Update Pasive"));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> Delete(@PathVariable("id") String id) {
        log.info("[INI] Delete Pasive");
        log.info(id);

        return pasiveService.Delete(id)
                .flatMap(o -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, null)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Error", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] Delete Pasive"));
    }


    @GetMapping("/client/{type}/{idClient}")
    public Mono<ResponseEntity<Object>> ExistByClientIdType(@PathVariable Integer type, @PathVariable String idClient)
    {
        log.info("[INI] ExistByClientIdType");
        return pasiveService.ExistByClientIdType(type,idClient)
                .flatMap(pasive -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, pasive.getId())))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Error", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] ExistByClientIdType"));
    }

    @GetMapping("/type/{id}")
    @TimeLimiter(name = RESILENCE_SERVICE)
    @CircuitBreaker(name = RESILENCE_SERVICE,fallbackMethod ="failedFindType")
    public Mono<ResponseEntity<Object>> FindType(@PathVariable String id) {
        log.info("[INI] Find Type Pasive");
        return FindTypeHelper.FindTypeSequence(log,parameterService,pasiveService,id)
                .doFinally(fin -> log.info("[END] Find Type Pasive"));

    }

    public Mono<ResponseEntity<Object>> failedFindType(String id, RuntimeException e)
    {
        log.error("[INIT] Failed FindType");
        log.error(e.getMessage());
        log.error(id);
        log.error("[END] Failed FindType");
        return Mono.just(ResponseHandler.response("Overcharged method", HttpStatus.PAYLOAD_TOO_LARGE, null));
    }

    @PutMapping("/debitCard/{idDebitCard}")
    public Mono<ResponseEntity<Object>> payWithDebitCard(@PathVariable String idDebitCard, @RequestBody Mont mont)
    {
        log.info("[INIT] payWithDebitCard");
        log.info(idDebitCard);
        log.info(mont.toString());

        return debitCardService.getDebitCardPasives(idDebitCard)
                .flatMap(responseDebitCard -> {
                    log.info(responseDebitCard.toString());
                    if(!responseDebitCard.getData().isEmpty())
                    {
                        var list = responseDebitCard.getData()
                                .stream()
                                .map(DebitCardPasive::getPasiveId)
                                .collect(Collectors.toList());

                        log.info(list.toString());

                        return pasiveService.FindAllById(list)
                                .collectList()
                                .flatMap(pasives -> {
                                    log.info("Obtain all pasives");
                                    log.info(pasives.toString());

                                    float currentMont = (float) pasives.stream().mapToDouble(Pasive::getMont).sum();

                                    log.info(currentMont+"");

                                    if(currentMont<mont.getMont())
                                        return Mono.just(ResponseHandler.response("You don't have enough credit", HttpStatus.BAD_REQUEST, false));
                                    else
                                    {
                                        var IdList = pasives.stream().map(pasive -> {
                                            float dif = pasive.getMont() + mont.getMont();

                                            log.info(dif+"");

                                            if (dif >= 0)
                                            {
                                                pasive.setMont(dif);
                                                mont.setMont(0f);
                                            }
                                            else
                                            {
                                                pasive.setMont(0f);
                                                mont.setMont(Math.abs(dif));
                                            }

                                            return pasive;

                                        }).collect(Collectors.toList());

                                        return pasiveService.SaveAll(IdList)
                                                .collectList()
                                                .flatMap(pasivesList ->
                                                {
                                                    log.info(pasivesList.toString());
                                                    if (!pasivesList.isEmpty())
                                                        return Mono.just(ResponseHandler.response("Done", HttpStatus.OK, true));
                                                    else
                                                        return Mono.just(ResponseHandler.response("Error", HttpStatus.BAD_REQUEST, false));
                                                });
                                    }
                                });
                    }
                    else
                    {
                        return Mono.just(ResponseHandler.response("You don't have debit card pasives", HttpStatus.NO_CONTENT, null));
                    }
                })
                .doFinally(fin -> log.info("[END] payWithDebitCard"));
    }
}

package com.bank.pasive.controllers;

import com.bank.pasive.controllers.helpers.FindTypeHelper;
import com.bank.pasive.handler.ResponseHandler;
import com.bank.pasive.models.documents.Pasive;
import com.bank.pasive.models.utils.Mont;
import com.bank.pasive.services.IClientService;
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


@RestController
@RequestMapping("/api/pasive")
public class PasiveControllers {

    @Autowired
    private IPasiveService pasiveService;

    @Autowired
    private IClientService parameterService;


    private static final Logger log = LoggerFactory.getLogger(PasiveControllers.class);
    private static final String RESILENCE_SERVICE = "defaultConfig";

    @PostMapping
    public Mono<ResponseEntity<Object>> Create(@Valid @RequestBody Pasive p) {
        log.info("[INI] Create Pasive");
        p.setCreatedDate(LocalDateTime.now());
        return pasiveService.Create(p)
                .doOnNext(pasive -> log.info(pasive.toString()))
                .flatMap(pasive -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, pasive)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
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
        return FindTypeHelper.FindTypeSequence(log,parameterService,pasiveService,id);

    }

    public Mono<ResponseEntity<Object>> failedFindType(String id, RuntimeException e)
    {
        log.error("[INIT] Failed FindType");
        log.error(e.getMessage());
        log.error(id);
        log.error("[END] Failed FindType");
        return Mono.just(ResponseHandler.response("Overcharged method", HttpStatus.PAYLOAD_TOO_LARGE, null));
    }
}

package com.bank.pasive.controllers;

import com.bank.pasive.handler.ResponseHandler;
import com.bank.pasive.models.dao.PasiveDao;
import com.bank.pasive.models.documents.Parameter;
import com.bank.pasive.models.documents.Pasive;
import com.bank.pasive.models.utils.ResponseParameter;
import com.bank.pasive.services.ParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/pasive")
public class PasiveControllers {

    @Autowired
    private PasiveDao dao;

    @Autowired
    private ParameterService parameterClientService;

    private static final Logger log = LoggerFactory.getLogger(PasiveControllers.class);


    @PostMapping
    public Mono<ResponseEntity<Object>> Create(@RequestBody Pasive p) {
        log.info("[INI] Create Pasive");
        return dao.save(p)
                .doOnNext(pasive -> log.info(pasive.toString()))
                        .map(pasive -> ResponseHandler.response("Done", HttpStatus.OK, pasive))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] Create Pasive"));
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> FindAll() {
        log.info("[INI] FindAll Pasive");

        Mono<ResponseEntity<Object>>  responseEntityMono= dao.findAll()
                .doOnNext(person -> log.info(person.toString()))
                .collectList().map(pasives -> ResponseHandler.response("Done", HttpStatus.OK, pasives))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] FindAll Pasive"));

        return responseEntityMono;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> Find(@PathVariable String id) {
        log.info("[INI] Find Pasive");
        return dao.findById(id)
                .doOnNext(pasive -> log.info(pasive.toString()))
                .map(pasive -> ResponseHandler.response("Done", HttpStatus.OK, pasive))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] Find Pasive"));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> Update(@PathVariable("id") String id, @RequestBody Pasive p) {
        log.info("[INI] Update Pasive");
        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.save(p)
                        .doOnNext(pasive -> log.info(pasive.toString()))
                        .map(pasive -> ResponseHandler.response("Done", HttpStatus.OK, pasive))
                        .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
            else
                return Mono.just(ResponseHandler.response("Not found", HttpStatus.NOT_FOUND, null));

        })
        .doFinally(fin -> log.info("[END] Update Pasive"));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> Delete(@PathVariable("id") String id) {
        log.info("[INI] Delete Pasive");
        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.deleteById(id).then(Mono.just(ResponseHandler.response("Done", HttpStatus.OK, null)));
            else
                return Mono.just(ResponseHandler.response("Not found", HttpStatus.NOT_FOUND, null));
        })
        .doFinally(fin -> log.info("[END] Delete Pasive"));
    }

    @GetMapping("/test-parameter")
    public Mono<ResponseEntity<Object>> getParamter() {
        log.info("[INI] Paramter Pasive");

        Flux<List<Parameter>> parameterFlux = parameterClientService.getAllUsers()
                .map(data -> data.getData()).flux();

        Mono<ResponseEntity<Object>>  responseEntityMono= dao.findAll()
                .doOnNext(person -> log.info(person.toString()))
                .collectList().map(pasives -> ResponseHandler.response("Done", HttpStatus.OK, pasives))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] Paramter Pasive"));

        return responseEntityMono;
    }
}

package com.bank.pasive.controllers;

import com.bank.pasive.handler.ResponseHandler;
import com.bank.pasive.models.dao.PasiveDao;
import com.bank.pasive.models.documents.Pasive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/pasive")
public class PasiveControllers {

    @Autowired
    private PasiveDao dao;

    private static final Logger log = LoggerFactory.getLogger(PasiveControllers.class);

    @PostMapping
    public Mono<ResponseEntity<Object>> Create(@RequestBody Pasive p) {

        return dao.save(p)
                .doOnNext(pasive -> log.info(pasive.toString()))
                        .map(pasive -> ResponseHandler.response("Done", HttpStatus.OK, pasive))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> FindAll() {

        return dao.findAll()
                .doOnNext(person -> log.info(person.toString()))
                .collectList().map(pasives -> ResponseHandler.response("Done", HttpStatus.OK, pasives))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));

    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> Find(@PathVariable String id) {
        return dao.findById(id)
                .doOnNext(pasive -> log.info(pasive.toString()))
                .map(pasive -> ResponseHandler.response("Done", HttpStatus.OK, pasive))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> Update(@PathVariable("id") String id, @RequestBody Pasive p) {
        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.save(p)
                        .doOnNext(pasive -> log.info(pasive.toString()))
                        .map(pasive -> ResponseHandler.response("Done", HttpStatus.OK, pasive))
                        .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
            else
                return Mono.just(ResponseHandler.response("Not found", HttpStatus.NOT_FOUND, null));

        });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> Delete(@PathVariable("id") String id) {

        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.deleteById(id).then(Mono.just(ResponseHandler.response("Done", HttpStatus.OK, null)));
            else
                return Mono.just(ResponseHandler.response("Not found", HttpStatus.NOT_FOUND, null));
        });
    }
}

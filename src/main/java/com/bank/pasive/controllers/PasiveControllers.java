package com.bank.pasive.controllers;

import com.bank.pasive.controllers.helpers.FindTypeHelper;
import com.bank.pasive.controllers.helpers.UpgradePYMEHelper;
import com.bank.pasive.controllers.helpers.UpgradeVIPHelper;
import com.bank.pasive.handler.ResponseHandler;
import com.bank.pasive.models.documents.Pasive;
import com.bank.pasive.models.utils.Mont;
import com.bank.pasive.services.IActiveService;
import com.bank.pasive.services.IMovementService;
import com.bank.pasive.services.IParameterService;
import com.bank.pasive.services.IPasiveService;
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
    private IParameterService parameterService;

    @Autowired
    private IMovementService movementService;

    @Autowired
    private IActiveService activeService;

    private static final Logger log = LoggerFactory.getLogger(PasiveControllers.class);


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
                .flatMap(o -> Mono.just(ResponseHandler.response("Done", HttpStatus.NO_CONTENT, null)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Error", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] Delete Pasive"));
    }

    @GetMapping("/type/{id}")
    public Mono<ResponseEntity<Object>> FindType(@PathVariable String id) {
        log.info("[INI] Find Type Pasive");
        return FindTypeHelper.FindTypeSequence(log,parameterService,pasiveService,id);

    }

    @GetMapping("/upgrade/vip/{id}")
    public Mono<ResponseEntity<Object>> UpgradeVIP(@PathVariable("id") String id)
    {
        log.info("[INI] Upgrade VIP");

        return UpgradeVIPHelper.UpdateVIPSequence(log,pasiveService,movementService,activeService,id);

    }

    @GetMapping("/upgrade/pyme/{id}")
    public Mono<ResponseEntity<Object>> UpgradePYME(@PathVariable("id") String id)
    {
        log.info("[INI] Upgrade PYME");

        return UpgradePYMEHelper.UpdatePYMESequence(log,pasiveService,activeService,id);
    }
}

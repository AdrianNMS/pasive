package com.bank.pasive.services.impl;

import com.bank.pasive.models.dao.IPasiveDao;
import com.bank.pasive.models.documents.Pasive;
import com.bank.pasive.models.utils.Mont;
import com.bank.pasive.services.IPasiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PasiveImpl implements IPasiveService
{
    @Autowired
    private IPasiveDao dao;

    @Override
    public Mono<List<Pasive>> FindAll() {
        return dao.findAll().collectList();
    }

    @Override
    public Mono<Pasive> Find(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<Pasive> Create(Pasive pasive) {
        return dao.save(pasive);
    }

    @Override
    public Mono<Pasive> Update(String id, Pasive pasive) {
        return dao.existsById(id).flatMap(check ->
        {
            if (check)
            {
                return dao.save(pasive);
            }
            else
                return Mono.empty();

        });
    }

    @Override
    public Mono<Object> Delete(String id) {
        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.deleteById(id).then(Mono.just(true));
            else
                return Mono.empty();
        });
    }

    @Override
    public Mono<Pasive> SetMont(String id, Mont mont) {
        return Find(id)
                .flatMap(p -> {
                    p.setMont(p.getMont()+mont.getMont());
                    return Update(id,p);
                });
    }

    @Override
    public Mono<Mont> GetMont(String id) {
        return Find(id)
                .flatMap(pasive -> {
                    Mont mont = new Mont();
                    mont.setMont(pasive.getMont());

                    return Mono.just(mont);
                });
    }

    @Override
    public Mono<Pasive> ExistByClientIdType(Integer type, String id) {

        return FindAll().flatMap(pas->
           Mono.just(Objects.requireNonNull(pas.stream()
                   .filter(pasive -> pasive.getClientId().equals(id) && pasive.getPasivesType().getValue() == type)
                   .findFirst().orElse(null))
           )
        );
    }




}

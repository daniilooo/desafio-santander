package com.santander.desafio.service;

import com.santander.desafio.dto.AgenciaCadastroRequest;
import com.santander.desafio.dto.DistanciaResponse;
import com.santander.desafio.model.Agencia;
import com.santander.desafio.repository.AgenciaRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgenciaService {

    private final AgenciaRepository repository;

    public AgenciaService(AgenciaRepository repository) {
        this.repository = repository;
    }

    public Agencia cadastrar(AgenciaCadastroRequest req) {
        Agencia a = new Agencia();
        a.setPosX(req.posX());
        a.setPosY(req.posY());
        a.setNome("PENDENTE");
        Agencia salvo = repository.save(a);
        salvo.setNome("AGENCIA_" + salvo.getId());
        salvo = repository.save(salvo);
        // Coloca/atualiza no cache individual
        cachePutAgencia(salvo);
        return salvo;
    }

    @CachePut(value = "agencia", key = "#agencia.id")
    public Agencia cachePutAgencia(Agencia agencia) {
        return agencia;
    }

    @Cacheable(value = "agencia", key = "#id")
    public Agencia buscarPorIdCache(Long id) {
        // Será chamado apenas no miss do cache
        return repository.findById(id).orElseThrow();
    }

    public List<DistanciaResponse> calcularDistancias(double posX, double posY) {
        // Obtém todos IDs do banco e consulta cada um via cache (popula LRU com máx 10)
        return repository.findAll().stream()
                .map(a -> buscarPorIdCache(a.getId()))
                .map(a -> new DistanciaResponse(
                        String.valueOf(a.getId()),
                        distancia(posX, posY, a.getPosX(), a.getPosY())
                ))
                .sorted(Comparator.comparingDouble(DistanciaResponse::distancia))
                .collect(Collectors.toList());
    }

    private double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}

package com.santander.desafio.controller;

import com.santander.desafio.dto.AgenciaCadastroRequest;
import com.santander.desafio.dto.DistanciaResponse;
import com.santander.desafio.model.Agencia;
import com.santander.desafio.service.AgenciaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/desafio")
public class DesafioController {

    private final AgenciaService service;

    public DesafioController(AgenciaService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Agencia> cadastrar(@Valid @RequestBody AgenciaCadastroRequest request) {
        Agencia salvo = service.cadastrar(request);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/distancia")
    public ResponseEntity<List<DistanciaResponse>> distancia(@RequestParam double posX,
                                                             @RequestParam double posY) {
        return ResponseEntity.ok(service.calcularDistancias(posX, posY));
    }
}

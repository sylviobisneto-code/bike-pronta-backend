
        package org.example.bikeprontabackend.controller;

import org.example.bikeprontabackend.entity.Bicicleta;
import org.example.bikeprontabackend.service.BicicletaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bicicletas")
@CrossOrigin(
        origins = {
                "https://bikeprontajpa.com.br",
                "https://www.bikeprontajpa.com.br",
                "http://localhost:3000",
                "http://127.0.0.1:5500",
                "http://localhost:5500"
        }
)
public class BicicletaController {

    private final BicicletaService service;

    public BicicletaController(BicicletaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Bicicleta> listarTodas(
            @RequestParam(required = false) String categoria) {

        if (categoria != null && !categoria.isBlank()) {
            return service.listarPorCategoria(categoria);
        }

        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bicicleta> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Bicicleta salvar(@RequestBody Bicicleta bicicleta) {
        return service.salvar(bicicleta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bicicleta> atualizar(
            @PathVariable Long id,
            @RequestBody Bicicleta bicicleta) {

        return service.buscarPorId(id)
                .map(bicicletaExistente -> {
                    bicicletaExistente.setCategoria(
                            bicicleta.getCategoria());
                    bicicletaExistente.setNome(bicicleta.getNome());
                    bicicletaExistente.setMarca(bicicleta.getMarca());
                    bicicletaExistente.setModelo(bicicleta.getModelo());
                    bicicletaExistente.setDescricao(
                            bicicleta.getDescricao());
                    bicicletaExistente.setPreco(bicicleta.getPreco());
                    bicicletaExistente.setTipo(bicicleta.getTipo());
                    bicicletaExistente.setTamanho(
                            bicicleta.getTamanho());
                    bicicletaExistente.setAno(bicicleta.getAno());
                    bicicletaExistente.setImagem(bicicleta.getImagem());

                    return ResponseEntity.ok(
                            service.salvar(bicicletaExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}


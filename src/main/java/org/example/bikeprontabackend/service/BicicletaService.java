package org.example.bikeprontabackend.service;

import org.example.bikeprontabackend.entity.Bicicleta;
import org.example.bikeprontabackend.repository.BicicletaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BicicletaService {


    private final BicicletaRepository repository;

    public BicicletaService(BicicletaRepository repository) {
        this.repository = repository;
    }

    public List<Bicicleta> listarTodas() {
        return repository.findAll();
    }

    public List<Bicicleta> listarPorCategoria(String categoria) {
        return repository.findByCategoriaIgnoreCase(categoria);
    }

    public Optional<Bicicleta> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Bicicleta salvar(Bicicleta bicicleta) {
        return repository.save(bicicleta);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }


}

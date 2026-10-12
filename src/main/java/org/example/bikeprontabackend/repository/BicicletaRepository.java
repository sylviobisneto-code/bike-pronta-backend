package org.example.bikeprontabackend.repository;

import org.example.bikeprontabackend.entity.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {


    List<Bicicleta> findByCategoriaIgnoreCase(String categoria);


}

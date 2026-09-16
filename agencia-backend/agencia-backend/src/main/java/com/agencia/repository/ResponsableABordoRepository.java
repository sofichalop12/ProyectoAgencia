package com.agencia.repository;

import com.agencia.model.ResponsableABordo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponsableABordoRepository extends JpaRepository<ResponsableABordo, Long> {
    Optional<ResponsableABordo> findByDni(String dni);
}
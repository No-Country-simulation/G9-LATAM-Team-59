package com.financeai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financeai.models.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

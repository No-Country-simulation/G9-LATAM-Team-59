package com.financeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.financeai.models.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByEmail(String email);
}

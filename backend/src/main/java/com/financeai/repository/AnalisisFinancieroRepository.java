package com.financeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financeai.models.AnalisisFinanciero;

public interface AnalisisFinancieroRepository extends JpaRepository<AnalisisFinanciero, Long> {
    
}

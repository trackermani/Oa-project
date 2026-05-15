package com.seanergy.oa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seanergy.oa.domain.Oil;

public interface OilRepository extends JpaRepository<Oil, Integer> {

    Optional<Oil> findByOilId(Integer oilId);
}

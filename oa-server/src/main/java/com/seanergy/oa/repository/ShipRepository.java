package com.seanergy.oa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seanergy.oa.domain.Ship;

public interface ShipRepository extends JpaRepository<Ship, Integer> {

    Optional<Ship> findByShipId(Integer shipId);
}

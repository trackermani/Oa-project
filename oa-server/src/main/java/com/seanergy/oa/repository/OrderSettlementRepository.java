package com.seanergy.oa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seanergy.oa.domain.OrderSettlement;

public interface OrderSettlementRepository extends JpaRepository<OrderSettlement, Long> {

    Optional<OrderSettlement> findByOrderSettlementId(Long orderSettlementId);
}

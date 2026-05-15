package com.seanergy.oa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seanergy.oa.domain.OrderOilSupplier;

public interface OrderOilSupplierRepository extends JpaRepository<OrderOilSupplier, Long> {

    List<OrderOilSupplier> findByOrderOilIdAndIsActivatedTrue(Long orderOilId);
}

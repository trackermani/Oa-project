package com.seanergy.oa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seanergy.oa.domain.OrderOil;

public interface OrderOilRepository extends JpaRepository<OrderOil, Long> {

    List<OrderOil> findByOrderInfoIdAndIsActivatedTrue(Long orderInfoId);
}

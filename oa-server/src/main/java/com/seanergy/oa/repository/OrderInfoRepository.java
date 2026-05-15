package com.seanergy.oa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seanergy.oa.domain.OrderInfo;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {

    @Query("SELECT o FROM OrderInfo o WHERE o.mainOrderNum = :mainOrderNum AND o.isActivated = true")
    Optional<OrderInfo> findByMainOrderNum(@Param("mainOrderNum") Integer mainOrderNum);
}

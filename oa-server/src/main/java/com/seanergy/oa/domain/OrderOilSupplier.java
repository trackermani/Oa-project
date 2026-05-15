package com.seanergy.oa.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "OrderOilSupplier")
@Getter
public class OrderOilSupplier {

    @Id
    @Column(name = "OrderOilSupplierID")
    private Long orderOilSupplierId;

    @Column(name = "OrderOilID")
    private Long orderOilId;

    @Column(name = "OilSalesCompanyID")
    private Integer oilSalesCompanyId;

    @Column(name = "OilSalesCompanyName")
    private String oilSalesCompanyName;

    @Column(name = "BargeShipName")
    private String bargeShipName;

    @Column(name = "EstSupplyStartDate")
    private LocalDateTime estSupplyStartDate;

    @Column(name = "EstSupplyEndDate")
    private LocalDateTime estSupplyEndDate;

    @Column(name = "OilProvideCompleteDate")
    private LocalDateTime oilProvideCompleteDate;

    @Column(name = "IsActivated")
    private Boolean isActivated;
}

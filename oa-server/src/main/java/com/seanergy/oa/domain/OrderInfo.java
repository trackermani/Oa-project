package com.seanergy.oa.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "OrderInfo")
@Getter
public class OrderInfo {

    @Id
    @Column(name = "OrderInfoID")
    private Long orderInfoId;

    @Column(name = "MainOrderNum")
    private Integer mainOrderNum;

    @Column(name = "SubOrderNum")
    private String subOrderNum;

    @Column(name = "PortName")
    private String portName;

    @Column(name = "PortCountry")
    private String portCountry;

    @Column(name = "ReqSupplyStartDate")
    private LocalDateTime reqSupplyStartDate;

    @Column(name = "ReqSupplyEndDate")
    private LocalDateTime reqSupplyEndDate;

    @Column(name = "EstSupplyStartDate")
    private LocalDateTime estSupplyStartDate;

    @Column(name = "EstSupplyEndDate")
    private LocalDateTime estSupplyEndDate;

    @Column(name = "SupplyCompleteDate")
    private LocalDateTime supplyCompleteDate;

    @Column(name = "ShipID")
    private Integer shipId;

    @Column(name = "ShipCompanyID")
    private Integer shipCompanyId;

    @Column(name = "StatusCode")
    private Long statusCode;

    @Column(name = "OrderSettlementID")
    private Long orderSettlementId;

    @Column(name = "IsActivated")
    private Boolean isActivated;

    @Column(name = "RelatedAdminCompanyID")
    private Integer relatedAdminCompanyId;

    @Column(name = "Eta")
    private LocalDateTime eta;
}

package com.seanergy.oa.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "OrderOil")
@Getter
public class OrderOil {

    @Id
    @Column(name = "OrderOilID")
    private Long orderOilId;

    @Column(name = "OrderInfoID")
    private Long orderInfoId;

    @Column(name = "OilID")
    private Integer oilId;

    @Column(name = "MinAmount")
    private BigDecimal minAmount;

    @Column(name = "MaxAmount")
    private BigDecimal maxAmount;

    @Column(name = "DecideAmount")
    private BigDecimal decideAmount;

    @Column(name = "BuyPrice")
    private BigDecimal buyPrice;

    @Column(name = "SellPrice")
    private BigDecimal sellPrice;

    @Column(name = "OilProducer")
    private String oilProducer;

    @Column(name = "IsActivated")
    private Boolean isActivated;
}

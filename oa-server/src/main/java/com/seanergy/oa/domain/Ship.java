package com.seanergy.oa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "Ship")
@Getter
public class Ship {

    @Id
    @Column(name = "ShipID")
    private Integer shipId;

    @Column(name = "ShipName")
    private String shipName;

    @Column(name = "IMO")
    private String imo;

    @Column(name = "ShipType")
    private String shipType;

    @Column(name = "ShipCompanyID")
    private Integer shipCompanyId;
}

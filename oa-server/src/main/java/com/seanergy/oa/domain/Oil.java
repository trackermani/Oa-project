package com.seanergy.oa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "Oil")
@Getter
public class Oil {

    @Id
    @Column(name = "OilID")
    private Integer oilId;

    @Column(name = "Category")
    private String category;

    @Column(name = "OilName")
    private String oilName;

    @Column(name = "Specification")
    private String specification;
}

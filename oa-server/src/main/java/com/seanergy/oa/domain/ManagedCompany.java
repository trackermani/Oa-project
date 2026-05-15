package com.seanergy.oa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "ManagedCompany")
@Getter
public class ManagedCompany {

    @Id
    @Column(name = "ManagedCompanyID")
    private Integer managedCompanyId;

    @Column(name = "CompanyName")
    private String companyName;

    @Column(name = "LegalEntityName")
    private String legalEntityName;

    @Column(name = "BankName")
    private String bankName;

    @Column(name = "BankAccountNum")
    private String bankAccountNum;

    @Column(name = "BankAccountOwnerName")
    private String bankAccountOwnerName;

    @Column(name = "ContactEmail")
    private String contactEmail;

    @Column(name = "IsActivated")
    private Boolean isActivated;
}

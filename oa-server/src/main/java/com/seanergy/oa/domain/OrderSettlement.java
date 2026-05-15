package com.seanergy.oa.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "OrderSettlement")
@Getter
public class OrderSettlement {

    @Id
    @Column(name = "OrderSettlementID")
    private Long orderSettlementId;

    @Column(name = "PayType")
    private String payType;

    @Column(name = "TotalSalesAmount")
    private BigDecimal totalSalesAmount;

    @Column(name = "TotalPurchaseAmount")
    private BigDecimal totalPurchaseAmount;

    @Column(name = "IsIssueInvoice")
    private Boolean isIssueInvoice;

    @Column(name = "IsRecvBdr")
    private Boolean isRecvBdr;

    @Column(name = "PayDueDate")
    private LocalDateTime payDueDate;

    @Column(name = "PayCompleteDate")
    private LocalDateTime payCompleteDate;

    @Column(name = "BillType")
    private String billType;
}

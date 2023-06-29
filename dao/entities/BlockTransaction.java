package com.freecharge.financial.dao.entities;

import com.freecharge.financial.dto.enums.BillerCategoryType;
import com.freecharge.financial.dto.enums.OrderStatus;
import com.freecharge.financial.dto.enums.TransactionType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "block_transaction")
public class BlockTransaction implements Serializable {

    private static final long serialVersionUID = 45242202677765341L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregator_user_id")
    private Long aggregatorUserId;

    @Column(name = "txn_id")
    private String txnId;

    @Column(name = "aggregator_id")
    private Long aggregatorId;

    @Column(name = "raw_request")
    private String rawRequest;

    @Column(name = "raw_response")
    private String rawResponse;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "category")
    private BillerCategoryType category;

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus transactionStatus;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "metadata")
    private String metadata;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAggregatorUserId() {
        return aggregatorUserId;
    }

    public void setAggregatorUserId(Long aggregatorUserId) {
        this.aggregatorUserId = aggregatorUserId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public Long getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(Long aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    public String getRawRequest() {
        return rawRequest;
    }

    public void setRawRequest(String rawRequest) {
        this.rawRequest = rawRequest;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BillerCategoryType getCategory() {
        return category;
    }

    public void setCategory(BillerCategoryType category) {
        this.category = category;
    }

    public OrderStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(OrderStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BlockTransaction{");
        sb.append("id=").append(id);
        sb.append(", aggregatorUserId=").append(aggregatorUserId);
        sb.append(", txnId='").append(txnId).append('\'');
        sb.append(", aggregatorId=").append(aggregatorId);
        sb.append(", rawRequest='").append(rawRequest).append('\'');
        sb.append(", rawResponse='").append(rawResponse).append('\'');
        sb.append(", transactionType=").append(transactionType);
        sb.append(", category=").append(category);
        sb.append(", transactionStatus=").append(transactionStatus);
        sb.append(", amount=").append(amount);
        sb.append(", quantity=").append(quantity);
        sb.append(", metadata='").append(metadata).append('\'');
        sb.append(", created=").append(created);
        sb.append(", updated=").append(updated);
        sb.append('}');
        return sb.toString();
    }
}

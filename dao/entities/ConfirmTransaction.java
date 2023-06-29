package com.freecharge.financial.dao.entities;

import com.freecharge.financial.dto.enums.OrderStatus;
import com.freecharge.financial.dto.enums.TransactionType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "confirm_transaction")
public class ConfirmTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_id")
    private Long blockId;

    @Column(name = "raw_request")
    private String rawRequest;

    @Column(name = "raw_response")
    private String rawResponse;

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus transactionStatus;

    @Column(name = "invoice_id")
    private String invoiceId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "invoice_url")
    private String invoiceUrl;

    @Column(name = "metadata")
    private String metadata;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
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

    public OrderStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(OrderStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfirmTransaction{");
        sb.append("id=").append(id);
        sb.append(", blockId=").append(blockId);
        sb.append(", rawRequest='").append(rawRequest).append('\'');
        sb.append(", rawResponse='").append(rawResponse).append('\'');
        sb.append(", transactionStatus=").append(transactionStatus);
        sb.append(", invoiceId='").append(invoiceId).append('\'');
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", quantity=").append(quantity);
        sb.append(", transactionType=").append(transactionType);
        sb.append(", invoiceUrl='").append(invoiceUrl).append('\'');
        sb.append(", metadata='").append(metadata).append('\'');
        sb.append(", created=").append(created);
        sb.append(", updated=").append(updated);
        sb.append('}');
        return sb.toString();
    }
}

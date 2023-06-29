package com.freecharge.financial.dao.entities;

import com.freecharge.financial.dto.enums.OrderStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "sell_transaction_ledger")
public class SellTransactionLedger implements Serializable {

    
	private static final long serialVersionUID = 1289838267514530967L;

	@Id
    private Long txId;

    private Long blockId;
    
	private String orderId;

	private String bankReferenceNumber;
	
	private String accountNumber;

	private String bankName;

	private String ifscCode;
	
	private String txStatus;

    @Column(name = "transaction_status")
	@Enumerated(EnumType.STRING)
    private OrderStatus transactionStatus;

    @Column(name = "metadata")
    private String metadata;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = true, updatable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;


	public Long getBlockId() {
		return blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	public Long getTxId() {
		return txId;
	}

	public void setTxId(Long txId) {
		this.txId = txId;
	}

	public String getBankReferenceNumber() {
		return bankReferenceNumber;
	}

	public void setBankReferenceNumber(String bankReferenceNumber) {
		this.bankReferenceNumber = bankReferenceNumber;
	}

	public String getTxStatus() {
		return txStatus;
	}

	public void setTxStatus(String txStatus) {
		this.txStatus = txStatus;
	}

	public OrderStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(OrderStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	@Override
	public String toString() {
		return "SellTransactionLedger [blockId=" + blockId + ", txId=" + txId 
				+ ", orderId=" + orderId + ", bankReferenceNumber=" + bankReferenceNumber + ", accountNumber="
				+ accountNumber + ", bankName=" + bankName + ", ifscCode=" + ifscCode + ", txStatus=" + txStatus
				+ ", transactionStatus=" + transactionStatus + ", metadata=" + metadata + ", created=" + created
				+ ", updated=" + updated + "]";
	}

    
  }

package com.freecharge.financial.dao.entities;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "user_bank_account_details")
public class UserBankAccountDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private UserRegistration userId;


    @Column(name = "aggregator_user_id",nullable = false)
    private String aggregatorUserId;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "account_token")
    private String accountToken;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNo;

    @Column(name = "ifsc")
    private String ifscCode;

    @Column(name = "bank_status")
    private String bankStatus;

    @Column(columnDefinition = "TINYINT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isVerified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
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

    public UserBankAccountDetails(){};

    public UserRegistration getUserId() {
        return userId;
    }

    public void setUserId(UserRegistration userId) {
        this.userId = userId;
    }

    public String getAggregatorUserId() {
        return aggregatorUserId;
    }

    public void setAggregatorUserId(String aggregatorUserId) {
        this.aggregatorUserId = aggregatorUserId;
    }


    @Override
    public String toString() {
        return "UserBankAccountDetails{" +
                "id=" + id +
                ", userId=" + userId +
                ", aggregatorUserId='" + aggregatorUserId + '\'' +
                ", shortName='" + shortName + '\'' +
                ", accountToken='" + accountToken + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", bankStatus='" + bankStatus + '\'' +
                ", isVerified=" + isVerified +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}

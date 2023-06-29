package com.freecharge.financial.dao.entities;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity(name = "user_registration")
public class UserRegistration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imsUserName;
    private Integer imsUserId;
    private String imsEmailId;
    private String imsPhoneNo;
    private String aggregatorUserId;
    private BigDecimal goldBalance;
    private String accountNo;
    private String ifscCode;
    private String bankName;
    private Integer kycStatus;
    private String pincode;
    private String metaData;

    @OneToMany(fetch = FetchType.EAGER,mappedBy="aggregatorUserId",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserBankAccountDetails> banksDetails;

    @Column(columnDefinition = "TINYINT",nullable=false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isPhoneUpdated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getGoldBalance() {
        return goldBalance;
    }

    public void setGoldBalance(BigDecimal goldBalance) {
        this.goldBalance = goldBalance;
    }

    public String getImsUserName() {
        return imsUserName;
    }

    public void setImsUserName(String imsUserName) {
        this.imsUserName = imsUserName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getImsUserId() {
        return imsUserId;
    }

    public void setImsUserId(Integer imsUserId) {
        this.imsUserId = imsUserId;
    }

    public String getImsEmailId() {
        return imsEmailId;
    }

    public void setImsEmailId(String imsEmailId) {
        this.imsEmailId = imsEmailId;
    }

    public String getImsPhoneNo() {
        return imsPhoneNo;
    }

    public void setImsPhoneNo(String imsPhoneNo) {
        this.imsPhoneNo = imsPhoneNo;
    }

    public String getAggregatorUserId() {
        return aggregatorUserId;
    }

    public void setAggregatorUserId(String aggregatorUserId) {
        this.aggregatorUserId = aggregatorUserId;
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

    public Integer getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(Integer kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
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


    public Boolean getPhoneUpdated() {
        return isPhoneUpdated;
    }

    public void setPhoneUpdated(Boolean phoneUpdated) {
        isPhoneUpdated = phoneUpdated;
    }

    public List<UserBankAccountDetails> getBanksDetails() {
        return banksDetails;
    }

    public void setBanksDetails(List<UserBankAccountDetails> banksDetails) {
        this.banksDetails = banksDetails;
    }

    @Override
    public String toString() {
        return "UserRegistration{" +
                "id=" + id +
                ", imsUserName='" + imsUserName + '\'' +
                ", imsUserId=" + imsUserId +
                ", imsEmailId='" + imsEmailId + '\'' +
                ", imsPhoneNo='" + imsPhoneNo + '\'' +
                ", aggregatorUserId='" + aggregatorUserId + '\'' +
                ", goldBalance=" + goldBalance +
                ", accountNo='" + accountNo + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", kycStatus=" + kycStatus +
                ", pincode='" + pincode + '\'' +
                ", metaData='" + metaData + '\'' +
                ", banksDetails=" + banksDetails +
                ", isPhoneUpdated=" + isPhoneUpdated +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}

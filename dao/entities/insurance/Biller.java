package com.freecharge.financial.dao.entities.insurance;


import com.freecharge.financial.dto.enums.OptionalParams;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
public class Biller implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billerId;

    @NotNull
    @Size(max = 100)
    @Column(unique = true,nullable=false)
    private String billerCode;

    @NotNull
    @Size(max = 250)
    private String name;

    @Column(columnDefinition = "TINYINT",nullable=false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isActive;

    @Column(columnDefinition = "TINYINT",nullable=false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean bbpsEnabled;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biller_category")
    private BillerCategory billerCategory;

    private String imageUrl;

    private String defaultImageUrl;

    @Column(name="meta_data")
    @Convert(converter = HashMapConvertor.class)
    private Map<OptionalParams, Object> metaData;

    @OneToMany(fetch = FetchType.LAZY, mappedBy ="pk.biller", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "biller_id")
    private List<BillerAggregatorMapping> billerAggregatorMappings = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "biller_id")
    private List<InputValidator> inputValidators = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "biller_id")
    private List<OutputValidator> outputValidators = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", updatable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;
    

    @Column(columnDefinition = "TINYINT",nullable=false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean partialPaymentEnabled;
    
    @Enumerated(EnumType.STRING)
    private BillerType  billerType;
    
    private String sourceId;


    public Long getBillerId() {
        return billerId;
    }

    public void setBillerId(Long billerId) {
        this.billerId = billerId;
    }

    @NotNull
    public String getBillerCode() {
        return billerCode;
    }

    public void setBillerCode(@NotNull String billerCode) {
        this.billerCode = billerCode;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getBbpsEnabled() {
        return bbpsEnabled;
    }

    public void setBbpsEnabled(Boolean bbpsEnabled) {
        this.bbpsEnabled = bbpsEnabled;
    }


    @NotNull
    public BillerCategory getBillerCategory() {
        return billerCategory;
    }

    public void setBillerCategory(@NotNull BillerCategory billerCategory) {
        this.billerCategory = billerCategory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    public void setDefaultImageUrl(String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
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

    public List<BillerAggregatorMapping> getBillerAggregatorMappings() {
        return billerAggregatorMappings;
    }

    public void setBillerAggregatorMappings(List<BillerAggregatorMapping> billerAggregatorMappings) {
        this.billerAggregatorMappings = billerAggregatorMappings;
    }

    public List<InputValidator> getInputValidators() {
        return inputValidators;
    }

    public void setInputValidators(List<InputValidator> inputValidators) {
        this.inputValidators = inputValidators;
    }

    public List<OutputValidator> getOutputValidators() {
        return outputValidators;
    }

    public void setOutputValidators(List<OutputValidator> outputValidators) {
        this.outputValidators = outputValidators;
    }

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Map<OptionalParams, Object> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<OptionalParams, Object> metaData) {
		this.metaData = metaData;
	}
	
	

	public Boolean getPartialPaymentEnabled() {
		return partialPaymentEnabled;
	}

	public void setPartialPaymentEnabled(Boolean partialPaymentEnabled) {
		this.partialPaymentEnabled = partialPaymentEnabled;
	}

	public BillerType getBillerType() {
		return billerType;
	}

	public void setBillerType(BillerType billerType) {
		this.billerType = billerType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public String toString() {
		return "Biller [billerId=" + billerId + ", billerCode=" + billerCode + ", name=" + name + ", isActive="
				+ isActive + ", bbpsEnabled=" + bbpsEnabled + ", billerCategory=" + billerCategory + ", imageUrl="
				+ imageUrl + ", defaultImageUrl=" + defaultImageUrl + ", metaData=" + metaData
				+ ", billerAggregatorMappings=" + billerAggregatorMappings + ", inputValidators=" + inputValidators
				+ ", outputValidators=" + outputValidators + ", created=" + created + ", updated=" + updated
				+ ", partialPaymentEnabled=" + partialPaymentEnabled + ", billerType=" + billerType + ", sourceId="
				+ sourceId + "]";
	}

    
}

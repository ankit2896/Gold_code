package com.freecharge.financial.dao.entities.insurance;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "pk.biller",
                joinColumns = @JoinColumn(name = "biller_id")),
        @AssociationOverride(name = "pk.aggregators",
                joinColumns = @JoinColumn(name = "aggregator_id")) })
public class BillerAggregatorMapping implements Serializable {

    @EmbeddedId
    private BillerAggregatorId pk = new BillerAggregatorId();

    private Integer priority;

    @NotNull
    @Size(max = 100)
    private String billerCode;

    public BillerAggregatorId getPk() {
        return pk;
    }

    public void setPk(BillerAggregatorId pk) {
        this.pk = pk;
    }

    @Transient
    public Biller getBiller() {
        return getPk().getBiller();
    }

    public void setBiller(Biller biller) {
        getPk().setBiller(biller);
    }

    @Transient
    public Aggregators getAggregators() {
        return getPk().getAggregators();
    }

    public void setAggregators(Aggregators aggregators) {
        getPk().setAggregators(aggregators);
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @NotNull
    public String getBillerCode() {
        return billerCode;
    }

    public void setBillerCode(@NotNull String billerCode) {
        this.billerCode = billerCode;
    }
}


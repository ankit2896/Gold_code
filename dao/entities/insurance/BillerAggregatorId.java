package com.freecharge.financial.dao.entities.insurance;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class BillerAggregatorId implements Serializable {

    @ManyToOne
    private Biller biller;

    @ManyToOne
    private Aggregators aggregators ;

    public Biller getBiller() {
        return biller;
    }

    public void setBiller(Biller biller) {
        this.biller = biller;
    }

    public Aggregators getAggregators() {
        return aggregators;
    }

    public void setAggregators(Aggregators aggregators) {
        this.aggregators = aggregators;
    }


}

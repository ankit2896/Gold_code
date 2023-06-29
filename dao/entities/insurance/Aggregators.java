package com.freecharge.financial.dao.entities.insurance;

import com.freecharge.financial.base.Aggregator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Aggregators implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6724220267776594274L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aggregatorId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true,nullable=false)
    private Aggregator aggregatorName;

    @Column(columnDefinition = "TINYINT",nullable=false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.aggregators", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name = "aggregator_id")
    private List<BillerAggregatorMapping> billerAggregatorMappings = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", updatable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;

    public Long getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(Long aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    @NotNull
    public Aggregator getAggregatorName() {
        return aggregatorName;
    }

    public void setAggregatorName(@NotNull Aggregator aggregatorName) {
        this.aggregatorName = aggregatorName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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
}

package com.freecharge.financial.dao.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "delivery_txn_mapping")
public class DeliveryTransactionMapping implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "aggregator_user_id")
    private String aggregatorUserId;

    @Column(name = "ims_user_id")
    private String imsUserId;

    @Column(name = "address_id")
    private String addressId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "txn_id")
    private String txnId;

    @Column(name = "metadata")
    private String metadata;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;

    public DeliveryTransactionMapping() {

    }

}



package com.freecharge.financial.dao.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "buy_redeem_gold_txn_mapping")
public class BuyRedeemGoldTransactionMapping implements Serializable {
    private static final long serialVersionUID = 45242202677765341L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "aggregator_user_id")
    private String aggregatorUserId;

    @Column(name = "txn_id")
    private String txnId;

    @Column(name = "block_id")
    private String blockId;

    @Column(name = "metadata")
    private String metadata;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;
}

package com.freecharge.financial.dao.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "gms_gold_user")
public class GmsGoldUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "aggregator_user_id")
    private String aggregatorUserId;

    @Column(name = "goal_id")
    private String goalId;

    @Column(name = "gold_balance")
    private BigDecimal goldBalance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;

}

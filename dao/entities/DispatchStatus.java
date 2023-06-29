package com.freecharge.financial.dao.entities;

import com.freecharge.financial.dto.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="dispatch_status_details")
public class DispatchStatus implements Serializable {

    @Id
    @Column(name = "id")
    private long txnid;

    @Column(name = "aggregator_user_id")
    private String aggregatorUserId;

    @Column(name = "dispatch_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus deliveryStatus;

    @Column(name = "courier_tracking_id")
    private String courierTrackingId;

    @Column(name = "courier_company")
    private String courierCompanyName;

    @Column(name = "metadata")
    private String metadata;

    @Column(name="tracking_link")
    private String trackingLink;

    @Column(name = "created")
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "updated", columnDefinition = "DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" , nullable = false)
    @UpdateTimestamp
    private Date updated;
}

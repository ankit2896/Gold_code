package com.freecharge.financial.dao.entities.insurance;

import com.freecharge.financial.dto.enums.BillerCategoryType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BillerCategory implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7724723124666909112L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true,nullable=false)
    @Enumerated(EnumType.STRING)
    private BillerCategoryType billerCategoryType ;

    @Column(columnDefinition = "TINYINT",nullable=false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean enabled;

    @OneToMany(fetch = FetchType.EAGER,mappedBy="billerCategory")
   // @JoinColumn(name = "category_id")
    private List<Biller> billers ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public BillerCategoryType getBillerCategoryType() {
        return billerCategoryType;
    }

    public void setBillerCategoryType(@NotNull BillerCategoryType billerCategoryType) {
        this.billerCategoryType = billerCategoryType;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Biller> getBillers() {
        return billers;
    }

    public void setBillers(List<Biller> billers) {
        this.billers = billers;
    }
}

package com.freecharge.financial.dao.entities.insurance;


import com.freecharge.financial.base.BdkLabel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class OutputValidator implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 250)
    private String fcLabel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BdkLabel bdkLabel ;

    @NotNull
    private String message;

    @Size(max = 250)
    private String regex;

    @NotNull
    private Integer sortOrder;

    @NotNull
    private Boolean enabled;

    @NotNull
    private Boolean displable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getRegex() {
        return regex;
    }

    public void setRegex(@NotNull String regex) {
        this.regex = regex;
    }

    @NotNull
    public String getFcLabel() {
        return fcLabel;
    }

    public void setFcLabel(@NotNull String fcLabel) {
        this.fcLabel = fcLabel;
    }

    @NotNull
    public BdkLabel getBdkLabel() {
        return bdkLabel;
    }

    public void setBdkLabel(@NotNull BdkLabel bdkLabel) {
        this.bdkLabel = bdkLabel;
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }

    @NotNull
    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(@NotNull Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @NotNull
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(@NotNull Boolean enabled) {
        this.enabled = enabled;
    }

    @NotNull
    public Boolean getDisplable() {
        return displable;
    }

    public void setDisplable(@NotNull Boolean displable) {
        this.displable = displable;
    }
}

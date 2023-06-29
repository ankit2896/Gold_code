package com.freecharge.financial.dao.entities.insurance;

import com.freecharge.financial.base.BdkLabel;
import com.freecharge.financial.base.InputType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class InputValidator implements Serializable {

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
    @Enumerated(EnumType.STRING)
    private InputType inputType = InputType.Text;

    private String dateTimeFormat;

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
    public String getMessage() {
        return message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
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
    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(@NotNull Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @NotNull
    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(@NotNull InputType inputType) {
        this.inputType = inputType;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }
}

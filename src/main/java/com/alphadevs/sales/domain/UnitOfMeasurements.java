package com.alphadevs.sales.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * UnitOfMeasurements Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "UnitOfMeasurements Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "unit_of_measurements")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnitOfMeasurements implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "uom_code", nullable = false)
    private String uomCode;

    @NotNull
    @Column(name = "uom_description", nullable = false)
    private String uomDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUomCode() {
        return uomCode;
    }

    public UnitOfMeasurements uomCode(String uomCode) {
        this.uomCode = uomCode;
        return this;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomDescription() {
        return uomDescription;
    }

    public UnitOfMeasurements uomDescription(String uomDescription) {
        this.uomDescription = uomDescription;
        return this;
    }

    public void setUomDescription(String uomDescription) {
        this.uomDescription = uomDescription;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitOfMeasurements)) {
            return false;
        }
        return id != null && id.equals(((UnitOfMeasurements) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UnitOfMeasurements{" +
            "id=" + getId() +
            ", uomCode='" + getUomCode() + "'" +
            ", uomDescription='" + getUomDescription() + "'" +
            "}";
    }
}

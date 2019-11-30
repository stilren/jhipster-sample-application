package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Duration;

/**
 * A Revenue.
 */
@Entity
@Table(name = "revenue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Revenue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "revenue_per_tick")
    private String revenuePerTick;

    @Column(name = "cron_schedule")
    private String cronSchedule;

    @Column(name = "delay")
    private Duration delay;

    @OneToOne(mappedBy = "revenue")
    @JsonIgnore
    private Cohort cohort;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRevenuePerTick() {
        return revenuePerTick;
    }

    public Revenue revenuePerTick(String revenuePerTick) {
        this.revenuePerTick = revenuePerTick;
        return this;
    }

    public void setRevenuePerTick(String revenuePerTick) {
        this.revenuePerTick = revenuePerTick;
    }

    public String getCronSchedule() {
        return cronSchedule;
    }

    public Revenue cronSchedule(String cronSchedule) {
        this.cronSchedule = cronSchedule;
        return this;
    }

    public void setCronSchedule(String cronSchedule) {
        this.cronSchedule = cronSchedule;
    }

    public Duration getDelay() {
        return delay;
    }

    public Revenue delay(Duration delay) {
        this.delay = delay;
        return this;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    public Cohort getCohort() {
        return cohort;
    }

    public Revenue cohort(Cohort cohort) {
        this.cohort = cohort;
        return this;
    }

    public void setCohort(Cohort cohort) {
        this.cohort = cohort;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Revenue)) {
            return false;
        }
        return id != null && id.equals(((Revenue) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Revenue{" +
            "id=" + getId() +
            ", revenuePerTick='" + getRevenuePerTick() + "'" +
            ", cronSchedule='" + getCronSchedule() + "'" +
            ", delay='" + getDelay() + "'" +
            "}";
    }
}

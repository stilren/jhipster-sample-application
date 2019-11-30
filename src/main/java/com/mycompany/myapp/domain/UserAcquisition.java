package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Duration;

/**
 * A UserAcquisition.
 */
@Entity
@Table(name = "user_acquisition")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserAcquisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "users_acquired_per_tick")
    private String usersAcquiredPerTick;

    @Column(name = "cron_schedule")
    private String cronSchedule;

    @Column(name = "jhi_repeat")
    private Integer repeat;

    @Column(name = "delay")
    private Duration delay;

    @OneToOne(mappedBy = "userAcquisition")
    @JsonIgnore
    private Cohort cohort;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsersAcquiredPerTick() {
        return usersAcquiredPerTick;
    }

    public UserAcquisition usersAcquiredPerTick(String usersAcquiredPerTick) {
        this.usersAcquiredPerTick = usersAcquiredPerTick;
        return this;
    }

    public void setUsersAcquiredPerTick(String usersAcquiredPerTick) {
        this.usersAcquiredPerTick = usersAcquiredPerTick;
    }

    public String getCronSchedule() {
        return cronSchedule;
    }

    public UserAcquisition cronSchedule(String cronSchedule) {
        this.cronSchedule = cronSchedule;
        return this;
    }

    public void setCronSchedule(String cronSchedule) {
        this.cronSchedule = cronSchedule;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public UserAcquisition repeat(Integer repeat) {
        this.repeat = repeat;
        return this;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public Duration getDelay() {
        return delay;
    }

    public UserAcquisition delay(Duration delay) {
        this.delay = delay;
        return this;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    public Cohort getCohort() {
        return cohort;
    }

    public UserAcquisition cohort(Cohort cohort) {
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
        if (!(o instanceof UserAcquisition)) {
            return false;
        }
        return id != null && id.equals(((UserAcquisition) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserAcquisition{" +
            "id=" + getId() +
            ", usersAcquiredPerTick='" + getUsersAcquiredPerTick() + "'" +
            ", cronSchedule='" + getCronSchedule() + "'" +
            ", repeat=" + getRepeat() +
            ", delay='" + getDelay() + "'" +
            "}";
    }
}

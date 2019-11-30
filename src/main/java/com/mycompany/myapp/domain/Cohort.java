package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cohort.
 */
@Entity
@Table(name = "cohort")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cohort implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cohort_name")
    private String cohortName;

    @OneToOne
    @JoinColumn(unique = true)
    private UserAcquisition userAcquisition;

    @OneToOne
    @JoinColumn(unique = true)
    private Churn churn;

    @OneToOne
    @JoinColumn(unique = true)
    private Revenue revenue;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "cohort_scenario",
               joinColumns = @JoinColumn(name = "cohort_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "scenario_id", referencedColumnName = "id"))
    private Set<Scenario> scenarios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("cohorts")
    private Team team;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCohortName() {
        return cohortName;
    }

    public Cohort cohortName(String cohortName) {
        this.cohortName = cohortName;
        return this;
    }

    public void setCohortName(String cohortName) {
        this.cohortName = cohortName;
    }

    public UserAcquisition getUserAcquisition() {
        return userAcquisition;
    }

    public Cohort userAcquisition(UserAcquisition userAcquisition) {
        this.userAcquisition = userAcquisition;
        return this;
    }

    public void setUserAcquisition(UserAcquisition userAcquisition) {
        this.userAcquisition = userAcquisition;
    }

    public Churn getChurn() {
        return churn;
    }

    public Cohort churn(Churn churn) {
        this.churn = churn;
        return this;
    }

    public void setChurn(Churn churn) {
        this.churn = churn;
    }

    public Revenue getRevenue() {
        return revenue;
    }

    public Cohort revenue(Revenue revenue) {
        this.revenue = revenue;
        return this;
    }

    public void setRevenue(Revenue revenue) {
        this.revenue = revenue;
    }

    public Set<Scenario> getScenarios() {
        return scenarios;
    }

    public Cohort scenarios(Set<Scenario> scenarios) {
        this.scenarios = scenarios;
        return this;
    }

    public Cohort addScenario(Scenario scenario) {
        this.scenarios.add(scenario);
        scenario.getCohorts().add(this);
        return this;
    }

    public Cohort removeScenario(Scenario scenario) {
        this.scenarios.remove(scenario);
        scenario.getCohorts().remove(this);
        return this;
    }

    public void setScenarios(Set<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public Team getTeam() {
        return team;
    }

    public Cohort team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cohort)) {
            return false;
        }
        return id != null && id.equals(((Cohort) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cohort{" +
            "id=" + getId() +
            ", cohortName='" + getCohortName() + "'" +
            "}";
    }
}

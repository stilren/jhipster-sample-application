package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Scenario.
 */
@Entity
@Table(name = "scenario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Scenario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_name")
    private String scenarioName;

    @ManyToOne
    @JsonIgnoreProperties("scenarios")
    private Team team;

    @ManyToMany(mappedBy = "scenarios")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Cohort> cohorts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public Scenario scenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public Team getTeam() {
        return team;
    }

    public Scenario team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<Cohort> getCohorts() {
        return cohorts;
    }

    public Scenario cohorts(Set<Cohort> cohorts) {
        this.cohorts = cohorts;
        return this;
    }

    public Scenario addCohort(Cohort cohort) {
        this.cohorts.add(cohort);
        cohort.getScenarios().add(this);
        return this;
    }

    public Scenario removeCohort(Cohort cohort) {
        this.cohorts.remove(cohort);
        cohort.getScenarios().remove(this);
        return this;
    }

    public void setCohorts(Set<Cohort> cohorts) {
        this.cohorts = cohorts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Scenario)) {
            return false;
        }
        return id != null && id.equals(((Scenario) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Scenario{" +
            "id=" + getId() +
            ", scenarioName='" + getScenarioName() + "'" +
            "}";
    }
}

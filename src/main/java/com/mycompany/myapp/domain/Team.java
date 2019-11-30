package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name")
    private String teamName;

    @OneToMany(mappedBy = "team")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Scenario> scenarios = new HashSet<>();

    @OneToMany(mappedBy = "team")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Cohort> cohorts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public Team teamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<Scenario> getScenarios() {
        return scenarios;
    }

    public Team scenarios(Set<Scenario> scenarios) {
        this.scenarios = scenarios;
        return this;
    }

    public Team addScenario(Scenario scenario) {
        this.scenarios.add(scenario);
        scenario.setTeam(this);
        return this;
    }

    public Team removeScenario(Scenario scenario) {
        this.scenarios.remove(scenario);
        scenario.setTeam(null);
        return this;
    }

    public void setScenarios(Set<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public Set<Cohort> getCohorts() {
        return cohorts;
    }

    public Team cohorts(Set<Cohort> cohorts) {
        this.cohorts = cohorts;
        return this;
    }

    public Team addCohort(Cohort cohort) {
        this.cohorts.add(cohort);
        cohort.setTeam(this);
        return this;
    }

    public Team removeCohort(Cohort cohort) {
        this.cohorts.remove(cohort);
        cohort.setTeam(null);
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
        if (!(o instanceof Team)) {
            return false;
        }
        return id != null && id.equals(((Team) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", teamName='" + getTeamName() + "'" +
            "}";
    }
}

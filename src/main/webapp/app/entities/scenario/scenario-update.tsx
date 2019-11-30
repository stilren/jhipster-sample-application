import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ITeam } from 'app/shared/model/team.model';
import { getEntities as getTeams } from 'app/entities/team/team.reducer';
import { ICohort } from 'app/shared/model/cohort.model';
import { getEntities as getCohorts } from 'app/entities/cohort/cohort.reducer';
import { getEntity, updateEntity, createEntity, reset } from './scenario.reducer';
import { IScenario } from 'app/shared/model/scenario.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IScenarioUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IScenarioUpdateState {
  isNew: boolean;
  teamId: string;
  cohortId: string;
}

export class ScenarioUpdate extends React.Component<IScenarioUpdateProps, IScenarioUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      teamId: '0',
      cohortId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getTeams();
    this.props.getCohorts();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { scenarioEntity } = this.props;
      const entity = {
        ...scenarioEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/scenario');
  };

  render() {
    const { scenarioEntity, teams, cohorts, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhipsterSampleApplicationApp.scenario.home.createOrEditLabel">Create or edit a Scenario</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : scenarioEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="scenario-id">ID</Label>
                    <AvInput id="scenario-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="scenarioNameLabel" for="scenario-scenarioName">
                    Scenario Name
                  </Label>
                  <AvField id="scenario-scenarioName" type="text" name="scenarioName" />
                </AvGroup>
                <AvGroup>
                  <Label for="scenario-team">Team</Label>
                  <AvInput id="scenario-team" type="select" className="form-control" name="team.id">
                    <option value="" key="0" />
                    {teams
                      ? teams.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/scenario" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  teams: storeState.team.entities,
  cohorts: storeState.cohort.entities,
  scenarioEntity: storeState.scenario.entity,
  loading: storeState.scenario.loading,
  updating: storeState.scenario.updating,
  updateSuccess: storeState.scenario.updateSuccess
});

const mapDispatchToProps = {
  getTeams,
  getCohorts,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScenarioUpdate);

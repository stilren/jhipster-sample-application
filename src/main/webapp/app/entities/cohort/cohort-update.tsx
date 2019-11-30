import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUserAcquisition } from 'app/shared/model/user-acquisition.model';
import { getEntities as getUserAcquisitions } from 'app/entities/user-acquisition/user-acquisition.reducer';
import { IChurn } from 'app/shared/model/churn.model';
import { getEntities as getChurns } from 'app/entities/churn/churn.reducer';
import { IRevenue } from 'app/shared/model/revenue.model';
import { getEntities as getRevenues } from 'app/entities/revenue/revenue.reducer';
import { IScenario } from 'app/shared/model/scenario.model';
import { getEntities as getScenarios } from 'app/entities/scenario/scenario.reducer';
import { ITeam } from 'app/shared/model/team.model';
import { getEntities as getTeams } from 'app/entities/team/team.reducer';
import { getEntity, updateEntity, createEntity, reset } from './cohort.reducer';
import { ICohort } from 'app/shared/model/cohort.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICohortUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICohortUpdateState {
  isNew: boolean;
  idsscenario: any[];
  userAcquisitionId: string;
  churnId: string;
  revenueId: string;
  teamId: string;
}

export class CohortUpdate extends React.Component<ICohortUpdateProps, ICohortUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsscenario: [],
      userAcquisitionId: '0',
      churnId: '0',
      revenueId: '0',
      teamId: '0',
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

    this.props.getUserAcquisitions();
    this.props.getChurns();
    this.props.getRevenues();
    this.props.getScenarios();
    this.props.getTeams();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { cohortEntity } = this.props;
      const entity = {
        ...cohortEntity,
        ...values,
        scenarios: mapIdList(values.scenarios)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/cohort');
  };

  render() {
    const { cohortEntity, userAcquisitions, churns, revenues, scenarios, teams, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhipsterSampleApplicationApp.cohort.home.createOrEditLabel">Create or edit a Cohort</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : cohortEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="cohort-id">ID</Label>
                    <AvInput id="cohort-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="cohortNameLabel" for="cohort-cohortName">
                    Cohort Name
                  </Label>
                  <AvField id="cohort-cohortName" type="text" name="cohortName" />
                </AvGroup>
                <AvGroup>
                  <Label for="cohort-userAcquisition">User Acquisition</Label>
                  <AvInput id="cohort-userAcquisition" type="select" className="form-control" name="userAcquisition.id">
                    <option value="" key="0" />
                    {userAcquisitions
                      ? userAcquisitions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="cohort-churn">Churn</Label>
                  <AvInput id="cohort-churn" type="select" className="form-control" name="churn.id">
                    <option value="" key="0" />
                    {churns
                      ? churns.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="cohort-revenue">Revenue</Label>
                  <AvInput id="cohort-revenue" type="select" className="form-control" name="revenue.id">
                    <option value="" key="0" />
                    {revenues
                      ? revenues.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="cohort-scenario">Scenario</Label>
                  <AvInput
                    id="cohort-scenario"
                    type="select"
                    multiple
                    className="form-control"
                    name="scenarios"
                    value={cohortEntity.scenarios && cohortEntity.scenarios.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {scenarios
                      ? scenarios.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="cohort-team">Team</Label>
                  <AvInput id="cohort-team" type="select" className="form-control" name="team.id">
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
                <Button tag={Link} id="cancel-save" to="/cohort" replace color="info">
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
  userAcquisitions: storeState.userAcquisition.entities,
  churns: storeState.churn.entities,
  revenues: storeState.revenue.entities,
  scenarios: storeState.scenario.entities,
  teams: storeState.team.entities,
  cohortEntity: storeState.cohort.entity,
  loading: storeState.cohort.loading,
  updating: storeState.cohort.updating,
  updateSuccess: storeState.cohort.updateSuccess
});

const mapDispatchToProps = {
  getUserAcquisitions,
  getChurns,
  getRevenues,
  getScenarios,
  getTeams,
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
)(CohortUpdate);

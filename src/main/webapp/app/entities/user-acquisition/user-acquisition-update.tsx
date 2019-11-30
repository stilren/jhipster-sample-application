import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICohort } from 'app/shared/model/cohort.model';
import { getEntities as getCohorts } from 'app/entities/cohort/cohort.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-acquisition.reducer';
import { IUserAcquisition } from 'app/shared/model/user-acquisition.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserAcquisitionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IUserAcquisitionUpdateState {
  isNew: boolean;
  cohortId: string;
}

export class UserAcquisitionUpdate extends React.Component<IUserAcquisitionUpdateProps, IUserAcquisitionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getCohorts();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { userAcquisitionEntity } = this.props;
      const entity = {
        ...userAcquisitionEntity,
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
    this.props.history.push('/user-acquisition');
  };

  render() {
    const { userAcquisitionEntity, cohorts, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhipsterSampleApplicationApp.userAcquisition.home.createOrEditLabel">Create or edit a UserAcquisition</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : userAcquisitionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="user-acquisition-id">ID</Label>
                    <AvInput id="user-acquisition-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="usersAcquiredPerTickLabel" for="user-acquisition-usersAcquiredPerTick">
                    Users Acquired Per Tick
                  </Label>
                  <AvField id="user-acquisition-usersAcquiredPerTick" type="text" name="usersAcquiredPerTick" />
                </AvGroup>
                <AvGroup>
                  <Label id="cronScheduleLabel" for="user-acquisition-cronSchedule">
                    Cron Schedule
                  </Label>
                  <AvField id="user-acquisition-cronSchedule" type="text" name="cronSchedule" />
                </AvGroup>
                <AvGroup>
                  <Label id="repeatLabel" for="user-acquisition-repeat">
                    Repeat
                  </Label>
                  <AvField id="user-acquisition-repeat" type="string" className="form-control" name="repeat" />
                </AvGroup>
                <AvGroup>
                  <Label id="delayLabel" for="user-acquisition-delay">
                    Delay
                  </Label>
                  <AvField id="user-acquisition-delay" type="text" name="delay" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/user-acquisition" replace color="info">
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
  cohorts: storeState.cohort.entities,
  userAcquisitionEntity: storeState.userAcquisition.entity,
  loading: storeState.userAcquisition.loading,
  updating: storeState.userAcquisition.updating,
  updateSuccess: storeState.userAcquisition.updateSuccess
});

const mapDispatchToProps = {
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
)(UserAcquisitionUpdate);

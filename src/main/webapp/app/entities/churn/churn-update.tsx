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
import { getEntity, updateEntity, createEntity, reset } from './churn.reducer';
import { IChurn } from 'app/shared/model/churn.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IChurnUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IChurnUpdateState {
  isNew: boolean;
  cohortId: string;
}

export class ChurnUpdate extends React.Component<IChurnUpdateProps, IChurnUpdateState> {
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
      const { churnEntity } = this.props;
      const entity = {
        ...churnEntity,
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
    this.props.history.push('/churn');
  };

  render() {
    const { churnEntity, cohorts, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhipsterSampleApplicationApp.churn.home.createOrEditLabel">Create or edit a Churn</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : churnEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="churn-id">ID</Label>
                    <AvInput id="churn-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="chrunPerTickLabel" for="churn-chrunPerTick">
                    Chrun Per Tick
                  </Label>
                  <AvField id="churn-chrunPerTick" type="text" name="chrunPerTick" />
                </AvGroup>
                <AvGroup>
                  <Label id="cronScheduleLabel" for="churn-cronSchedule">
                    Cron Schedule
                  </Label>
                  <AvField id="churn-cronSchedule" type="text" name="cronSchedule" />
                </AvGroup>
                <AvGroup>
                  <Label id="delayLabel" for="churn-delay">
                    Delay
                  </Label>
                  <AvField id="churn-delay" type="text" name="delay" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/churn" replace color="info">
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
  churnEntity: storeState.churn.entity,
  loading: storeState.churn.loading,
  updating: storeState.churn.updating,
  updateSuccess: storeState.churn.updateSuccess
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
)(ChurnUpdate);

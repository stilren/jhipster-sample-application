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
import { getEntity, updateEntity, createEntity, reset } from './revenue.reducer';
import { IRevenue } from 'app/shared/model/revenue.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRevenueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRevenueUpdateState {
  isNew: boolean;
  cohortId: string;
}

export class RevenueUpdate extends React.Component<IRevenueUpdateProps, IRevenueUpdateState> {
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
      const { revenueEntity } = this.props;
      const entity = {
        ...revenueEntity,
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
    this.props.history.push('/revenue');
  };

  render() {
    const { revenueEntity, cohorts, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhipsterSampleApplicationApp.revenue.home.createOrEditLabel">Create or edit a Revenue</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : revenueEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="revenue-id">ID</Label>
                    <AvInput id="revenue-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="revenuePerTickLabel" for="revenue-revenuePerTick">
                    Revenue Per Tick
                  </Label>
                  <AvField id="revenue-revenuePerTick" type="text" name="revenuePerTick" />
                </AvGroup>
                <AvGroup>
                  <Label id="cronScheduleLabel" for="revenue-cronSchedule">
                    Cron Schedule
                  </Label>
                  <AvField id="revenue-cronSchedule" type="text" name="cronSchedule" />
                </AvGroup>
                <AvGroup>
                  <Label id="delayLabel" for="revenue-delay">
                    Delay
                  </Label>
                  <AvField id="revenue-delay" type="text" name="delay" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/revenue" replace color="info">
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
  revenueEntity: storeState.revenue.entity,
  loading: storeState.revenue.loading,
  updating: storeState.revenue.updating,
  updateSuccess: storeState.revenue.updateSuccess
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
)(RevenueUpdate);

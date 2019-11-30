import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './cohort.reducer';
import { ICohort } from 'app/shared/model/cohort.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICohortDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CohortDetail extends React.Component<ICohortDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { cohortEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Cohort [<b>{cohortEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="cohortName">Cohort Name</span>
            </dt>
            <dd>{cohortEntity.cohortName}</dd>
            <dt>User Acquisition</dt>
            <dd>{cohortEntity.userAcquisition ? cohortEntity.userAcquisition.id : ''}</dd>
            <dt>Churn</dt>
            <dd>{cohortEntity.churn ? cohortEntity.churn.id : ''}</dd>
            <dt>Revenue</dt>
            <dd>{cohortEntity.revenue ? cohortEntity.revenue.id : ''}</dd>
            <dt>Scenario</dt>
            <dd>
              {cohortEntity.scenarios
                ? cohortEntity.scenarios.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.id}</a>
                      {i === cohortEntity.scenarios.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
            <dt>Team</dt>
            <dd>{cohortEntity.team ? cohortEntity.team.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/cohort" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/cohort/${cohortEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ cohort }: IRootState) => ({
  cohortEntity: cohort.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CohortDetail);

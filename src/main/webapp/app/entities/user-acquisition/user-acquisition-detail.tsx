import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-acquisition.reducer';
import { IUserAcquisition } from 'app/shared/model/user-acquisition.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserAcquisitionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class UserAcquisitionDetail extends React.Component<IUserAcquisitionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userAcquisitionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            UserAcquisition [<b>{userAcquisitionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="usersAcquiredPerTick">Users Acquired Per Tick</span>
            </dt>
            <dd>{userAcquisitionEntity.usersAcquiredPerTick}</dd>
            <dt>
              <span id="cronSchedule">Cron Schedule</span>
            </dt>
            <dd>{userAcquisitionEntity.cronSchedule}</dd>
            <dt>
              <span id="repeat">Repeat</span>
            </dt>
            <dd>{userAcquisitionEntity.repeat}</dd>
            <dt>
              <span id="delay">Delay</span>
            </dt>
            <dd>{userAcquisitionEntity.delay}</dd>
          </dl>
          <Button tag={Link} to="/user-acquisition" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/user-acquisition/${userAcquisitionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ userAcquisition }: IRootState) => ({
  userAcquisitionEntity: userAcquisition.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserAcquisitionDetail);

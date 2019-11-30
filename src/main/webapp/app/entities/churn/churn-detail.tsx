import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './churn.reducer';
import { IChurn } from 'app/shared/model/churn.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IChurnDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ChurnDetail extends React.Component<IChurnDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { churnEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Churn [<b>{churnEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="chrunPerTick">Chrun Per Tick</span>
            </dt>
            <dd>{churnEntity.chrunPerTick}</dd>
            <dt>
              <span id="cronSchedule">Cron Schedule</span>
            </dt>
            <dd>{churnEntity.cronSchedule}</dd>
            <dt>
              <span id="delay">Delay</span>
            </dt>
            <dd>{churnEntity.delay}</dd>
          </dl>
          <Button tag={Link} to="/churn" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/churn/${churnEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ churn }: IRootState) => ({
  churnEntity: churn.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChurnDetail);

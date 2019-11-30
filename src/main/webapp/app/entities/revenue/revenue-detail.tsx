import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './revenue.reducer';
import { IRevenue } from 'app/shared/model/revenue.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRevenueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RevenueDetail extends React.Component<IRevenueDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { revenueEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Revenue [<b>{revenueEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="revenuePerTick">Revenue Per Tick</span>
            </dt>
            <dd>{revenueEntity.revenuePerTick}</dd>
            <dt>
              <span id="cronSchedule">Cron Schedule</span>
            </dt>
            <dd>{revenueEntity.cronSchedule}</dd>
            <dt>
              <span id="delay">Delay</span>
            </dt>
            <dd>{revenueEntity.delay}</dd>
          </dl>
          <Button tag={Link} to="/revenue" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/revenue/${revenueEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ revenue }: IRootState) => ({
  revenueEntity: revenue.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RevenueDetail);

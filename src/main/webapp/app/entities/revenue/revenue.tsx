import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './revenue.reducer';
import { IRevenue } from 'app/shared/model/revenue.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRevenueProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Revenue extends React.Component<IRevenueProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { revenueList, match } = this.props;
    return (
      <div>
        <h2 id="revenue-heading">
          Revenues
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Revenue
          </Link>
        </h2>
        <div className="table-responsive">
          {revenueList && revenueList.length > 0 ? (
            <Table responsive aria-describedby="revenue-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Revenue Per Tick</th>
                  <th>Cron Schedule</th>
                  <th>Delay</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {revenueList.map((revenue, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${revenue.id}`} color="link" size="sm">
                        {revenue.id}
                      </Button>
                    </td>
                    <td>{revenue.revenuePerTick}</td>
                    <td>{revenue.cronSchedule}</td>
                    <td>{revenue.delay}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${revenue.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${revenue.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${revenue.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Revenues found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ revenue }: IRootState) => ({
  revenueList: revenue.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Revenue);

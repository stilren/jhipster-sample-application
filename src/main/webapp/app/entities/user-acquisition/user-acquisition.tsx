import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './user-acquisition.reducer';
import { IUserAcquisition } from 'app/shared/model/user-acquisition.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserAcquisitionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class UserAcquisition extends React.Component<IUserAcquisitionProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { userAcquisitionList, match } = this.props;
    return (
      <div>
        <h2 id="user-acquisition-heading">
          User Acquisitions
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new User Acquisition
          </Link>
        </h2>
        <div className="table-responsive">
          {userAcquisitionList && userAcquisitionList.length > 0 ? (
            <Table responsive aria-describedby="user-acquisition-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Users Acquired Per Tick</th>
                  <th>Cron Schedule</th>
                  <th>Repeat</th>
                  <th>Delay</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {userAcquisitionList.map((userAcquisition, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${userAcquisition.id}`} color="link" size="sm">
                        {userAcquisition.id}
                      </Button>
                    </td>
                    <td>{userAcquisition.usersAcquiredPerTick}</td>
                    <td>{userAcquisition.cronSchedule}</td>
                    <td>{userAcquisition.repeat}</td>
                    <td>{userAcquisition.delay}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${userAcquisition.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${userAcquisition.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${userAcquisition.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No User Acquisitions found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ userAcquisition }: IRootState) => ({
  userAcquisitionList: userAcquisition.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserAcquisition);

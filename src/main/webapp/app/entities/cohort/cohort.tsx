import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './cohort.reducer';
import { ICohort } from 'app/shared/model/cohort.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface ICohortProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type ICohortState = IPaginationBaseState;

export class Cohort extends React.Component<ICohortProps, ICohortState> {
  state: ICohortState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { cohortList, match } = this.props;
    return (
      <div>
        <h2 id="cohort-heading">
          Cohorts
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Cohort
          </Link>
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            {cohortList && cohortList.length > 0 ? (
              <Table responsive aria-describedby="cohort-heading">
                <thead>
                  <tr>
                    <th className="hand" onClick={this.sort('id')}>
                      ID <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('cohortName')}>
                      Cohort Name <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      User Acquisition <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      Churn <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      Revenue <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      Team <FontAwesomeIcon icon="sort" />
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {cohortList.map((cohort, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`${match.url}/${cohort.id}`} color="link" size="sm">
                          {cohort.id}
                        </Button>
                      </td>
                      <td>{cohort.cohortName}</td>
                      <td>
                        {cohort.userAcquisition ? (
                          <Link to={`user-acquisition/${cohort.userAcquisition.id}`}>{cohort.userAcquisition.id}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td>{cohort.churn ? <Link to={`churn/${cohort.churn.id}`}>{cohort.churn.id}</Link> : ''}</td>
                      <td>{cohort.revenue ? <Link to={`revenue/${cohort.revenue.id}`}>{cohort.revenue.id}</Link> : ''}</td>
                      <td>{cohort.team ? <Link to={`team/${cohort.team.id}`}>{cohort.team.id}</Link> : ''}</td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`${match.url}/${cohort.id}`} color="info" size="sm">
                            <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${cohort.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${cohort.id}/delete`} color="danger" size="sm">
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="alert alert-warning">No Cohorts found</div>
            )}
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ cohort }: IRootState) => ({
  cohortList: cohort.entities,
  totalItems: cohort.totalItems,
  links: cohort.links,
  entity: cohort.entity,
  updateSuccess: cohort.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Cohort);

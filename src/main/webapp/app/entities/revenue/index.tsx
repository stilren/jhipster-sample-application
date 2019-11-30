import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Revenue from './revenue';
import RevenueDetail from './revenue-detail';
import RevenueUpdate from './revenue-update';
import RevenueDeleteDialog from './revenue-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RevenueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RevenueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RevenueDetail} />
      <ErrorBoundaryRoute path={match.url} component={Revenue} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RevenueDeleteDialog} />
  </>
);

export default Routes;

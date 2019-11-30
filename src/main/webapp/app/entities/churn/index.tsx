import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Churn from './churn';
import ChurnDetail from './churn-detail';
import ChurnUpdate from './churn-update';
import ChurnDeleteDialog from './churn-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ChurnUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ChurnUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ChurnDetail} />
      <ErrorBoundaryRoute path={match.url} component={Churn} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ChurnDeleteDialog} />
  </>
);

export default Routes;

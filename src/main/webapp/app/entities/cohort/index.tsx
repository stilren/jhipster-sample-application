import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cohort from './cohort';
import CohortDetail from './cohort-detail';
import CohortUpdate from './cohort-update';
import CohortDeleteDialog from './cohort-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CohortUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CohortUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CohortDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cohort} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CohortDeleteDialog} />
  </>
);

export default Routes;

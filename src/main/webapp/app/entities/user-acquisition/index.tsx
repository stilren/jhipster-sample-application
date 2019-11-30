import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserAcquisition from './user-acquisition';
import UserAcquisitionDetail from './user-acquisition-detail';
import UserAcquisitionUpdate from './user-acquisition-update';
import UserAcquisitionDeleteDialog from './user-acquisition-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserAcquisitionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserAcquisitionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserAcquisitionDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserAcquisition} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={UserAcquisitionDeleteDialog} />
  </>
);

export default Routes;

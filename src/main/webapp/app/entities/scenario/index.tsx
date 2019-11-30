import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Scenario from './scenario';
import ScenarioDetail from './scenario-detail';
import ScenarioUpdate from './scenario-update';
import ScenarioDeleteDialog from './scenario-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ScenarioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ScenarioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ScenarioDetail} />
      <ErrorBoundaryRoute path={match.url} component={Scenario} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ScenarioDeleteDialog} />
  </>
);

export default Routes;

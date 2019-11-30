import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cohort from './cohort';
import Scenario from './scenario';
import Team from './team';
import Revenue from './revenue';
import Churn from './churn';
import UserAcquisition from './user-acquisition';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}cohort`} component={Cohort} />
      <ErrorBoundaryRoute path={`${match.url}scenario`} component={Scenario} />
      <ErrorBoundaryRoute path={`${match.url}team`} component={Team} />
      <ErrorBoundaryRoute path={`${match.url}revenue`} component={Revenue} />
      <ErrorBoundaryRoute path={`${match.url}churn`} component={Churn} />
      <ErrorBoundaryRoute path={`${match.url}user-acquisition`} component={UserAcquisition} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;

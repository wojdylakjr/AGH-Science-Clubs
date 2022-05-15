import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ExtraUser from './extra-user';
import ExtraUserDetail from './extra-user-detail';
import ExtraUserUpdate from './extra-user-update';
import ExtraUserDeleteDialog from './extra-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExtraUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExtraUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExtraUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={ExtraUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExtraUserDeleteDialog} />
  </>
);

export default Routes;

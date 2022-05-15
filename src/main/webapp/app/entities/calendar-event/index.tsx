import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CalendarEvent from './calendar-event';
import CalendarEventDetail from './calendar-event-detail';
import CalendarEventUpdate from './calendar-event-update';
import CalendarEventDeleteDialog from './calendar-event-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CalendarEventUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CalendarEventUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CalendarEventDetail} />
      <ErrorBoundaryRoute path={match.url} component={CalendarEvent} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CalendarEventDeleteDialog} />
  </>
);

export default Routes;

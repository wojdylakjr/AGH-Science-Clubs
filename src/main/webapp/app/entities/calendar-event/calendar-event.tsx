import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICalendarEvent } from 'app/shared/model/calendar-event.model';
import { getEntities } from './calendar-event.reducer';

export const CalendarEvent = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const calendarEventList = useAppSelector(state => state.calendarEvent.entities);
  const loading = useAppSelector(state => state.calendarEvent.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="calendar-event-heading" data-cy="CalendarEventHeading">
        Calendar Events
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/calendar-event/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Calendar Event
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {calendarEventList && calendarEventList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Publication Date</th>
                <th>Description</th>
                <th>Link</th>
                <th>Image Url</th>
                <th>Status</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {calendarEventList.map((calendarEvent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/calendar-event/${calendarEvent.id}`} color="link" size="sm">
                      {calendarEvent.id}
                    </Button>
                  </td>
                  <td>{calendarEvent.title}</td>
                  <td>
                    {calendarEvent.startDate ? <TextFormat type="date" value={calendarEvent.startDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {calendarEvent.endDate ? <TextFormat type="date" value={calendarEvent.endDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {calendarEvent.publicationDate ? (
                      <TextFormat type="date" value={calendarEvent.publicationDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{calendarEvent.description}</td>
                  <td>{calendarEvent.link}</td>
                  <td>{calendarEvent.imageUrl}</td>
                  <td>{calendarEvent.status}</td>
                  <td>{calendarEvent.user ? calendarEvent.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/calendar-event/${calendarEvent.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/calendar-event/${calendarEvent.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/calendar-event/${calendarEvent.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Calendar Events found</div>
        )}
      </div>
    </div>
  );
};

export default CalendarEvent;

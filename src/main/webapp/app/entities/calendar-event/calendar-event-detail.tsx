import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './calendar-event.reducer';

export const CalendarEventDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const calendarEventEntity = useAppSelector(state => state.calendarEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="calendarEventDetailsHeading">CalendarEvent</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{calendarEventEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{calendarEventEntity.title}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>
            {calendarEventEntity.startDate ? (
              <TextFormat value={calendarEventEntity.startDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>
            {calendarEventEntity.endDate ? <TextFormat value={calendarEventEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="publicationDate">Publication Date</span>
          </dt>
          <dd>
            {calendarEventEntity.publicationDate ? (
              <TextFormat value={calendarEventEntity.publicationDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{calendarEventEntity.description}</dd>
          <dt>
            <span id="link">Link</span>
          </dt>
          <dd>{calendarEventEntity.link}</dd>
          <dt>
            <span id="imageUrl">Image Url</span>
          </dt>
          <dd>{calendarEventEntity.imageUrl}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{calendarEventEntity.status}</dd>
          <dt>User</dt>
          <dd>{calendarEventEntity.user ? calendarEventEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/calendar-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/calendar-event/${calendarEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CalendarEventDetail;

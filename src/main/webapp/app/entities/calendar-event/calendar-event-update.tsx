import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICalendarEvent } from 'app/shared/model/calendar-event.model';
import { getEntity, updateEntity, createEntity, reset } from './calendar-event.reducer';

export const CalendarEventUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const calendarEventEntity = useAppSelector(state => state.calendarEvent.entity);
  const loading = useAppSelector(state => state.calendarEvent.loading);
  const updating = useAppSelector(state => state.calendarEvent.updating);
  const updateSuccess = useAppSelector(state => state.calendarEvent.updateSuccess);
  const handleClose = () => {
    props.history.push('/calendar-event');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    values.publicationDate = convertDateTimeToServer(values.publicationDate);

    const entity = {
      ...calendarEventEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
          publicationDate: displayDefaultDateTime(),
        }
      : {
          ...calendarEventEntity,
          startDate: convertDateTimeFromServer(calendarEventEntity.startDate),
          endDate: convertDateTimeFromServer(calendarEventEntity.endDate),
          publicationDate: convertDateTimeFromServer(calendarEventEntity.publicationDate),
          user: calendarEventEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aghScienceClubsApp.calendarEvent.home.createOrEditLabel" data-cy="CalendarEventCreateUpdateHeading">
            Create or edit a CalendarEvent
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="calendar-event-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Title"
                id="calendar-event-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Start Date"
                id="calendar-event-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="End Date"
                id="calendar-event-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Publication Date"
                id="calendar-event-publicationDate"
                name="publicationDate"
                data-cy="publicationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Description" id="calendar-event-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Link" id="calendar-event-link" name="link" data-cy="link" type="text" />
              <ValidatedField label="Image Url" id="calendar-event-imageUrl" name="imageUrl" data-cy="imageUrl" type="text" />
              <ValidatedField label="Status" id="calendar-event-status" name="status" data-cy="status" type="text" />
              <ValidatedField id="calendar-event-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/calendar-event" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CalendarEventUpdate;

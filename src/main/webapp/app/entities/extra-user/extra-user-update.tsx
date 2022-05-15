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
import { IExtraUser } from 'app/shared/model/extra-user.model';
import { Blocks } from 'app/shared/model/enumerations/blocks.model';
import { Fields } from 'app/shared/model/enumerations/fields.model';
import { getEntity, updateEntity, createEntity, reset } from './extra-user.reducer';

export const ExtraUserUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const extraUserEntity = useAppSelector(state => state.extraUser.entity);
  const loading = useAppSelector(state => state.extraUser.loading);
  const updating = useAppSelector(state => state.extraUser.updating);
  const updateSuccess = useAppSelector(state => state.extraUser.updateSuccess);
  const blocksValues = Object.keys(Blocks);
  const fieldsValues = Object.keys(Fields);
  const handleClose = () => {
    props.history.push('/extra-user');
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
    const entity = {
      ...extraUserEntity,
      ...values,
      user: users.find(it => it.login === values.username),
      login: values.username,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          block: 'MATEMATYCZNY',
          field: 'ZIELONY',
          ...extraUserEntity,
          user: extraUserEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aghScienceClubsApp.extraUser.home.createOrEditLabel" data-cy="ExtraUserCreateUpdateHeading">
            Create or edit a ExtraUser
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm id="register-form" onSubmit={saveEntity}>
              <ValidatedField
                name="username"
                label="Username"
                placeholder={'Your username'}
                validate={{
                  required: { value: true, message: 'Your username is required.' },
                  pattern: {
                    value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                    message: 'Your username is invalid.',
                  },
                  minLength: { value: 1, message: 'Your username is required to be at least 1 character.' },
                  maxLength: { value: 50, message: 'Your username cannot be longer than 50 characters.' },
                }}
                data-cy="username"
              />
              <ValidatedField
                name="email"
                label="Email"
                placeholder={'Your email'}
                type="email"
                validate={{
                  required: { value: true, message: 'Your email is required.' },
                  minLength: { value: 5, message: 'Your email is required to be at least 5 characters.' },
                  maxLength: { value: 254, message: 'Your email cannot be longer than 50 characters.' },
                }}
                data-cy="email"
              />

              <ValidatedField label="Block" id="extra-user-block" name="block" data-cy="block" type="select">
                {blocksValues.map(block => (
                  <option value={block} key={block}>
                    {block}
                  </option>
                ))}
              </ValidatedField>

              <ValidatedField label="Field" id="extra-user-field" name="field" data-cy="field" type="select">
                {fieldsValues.map(fields => (
                  <option value={fields} key={fields}>
                    {fields}
                  </option>
                ))}
              </ValidatedField>

              <Button id="register-submit" color="primary" type="submit" data-cy="submit">
                Register
              </Button>
            </ValidatedForm>

            // <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
            //   {!isNew ? <ValidatedField name="id" required readOnly id="extra-user-id" label="ID" validate={{ required: true }} /> : null}
            //   <ValidatedField label="Block" id="extra-user-block" name="block" data-cy="block" type="select">
            //     {blocksValues.map(blocks => (
            //       <option value={blocks} key={blocks}>
            //         {blocks}
            //       </option>
            //     ))}
            //   </ValidatedField>
            //   <ValidatedField label="Field" id="extra-user-field" name="field" data-cy="field" type="select">
            //     {fieldsValues.map(fields => (
            //       <option value={fields} key={fields}>
            //         {fields}
            //       </option>
            //     ))}
            //   </ValidatedField>
            //   <ValidatedField id="extra-user-user" name="user" data-cy="user" label="User" type="select">
            //     <option value="" key="0" />
            //     {users
            //       ? users.map(otherEntity => (
            //           <option value={otherEntity.id} key={otherEntity.id}>
            //             {otherEntity.id}
            //           </option>
            //         ))
            //       : null}
            //   </ValidatedField>
            //   <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/extra-user" replace color="info">
            //     <FontAwesomeIcon icon="arrow-left" />
            //     &nbsp;
            //     <span className="d-none d-md-inline">Back</span>
            //   </Button>
            //   &nbsp;
            //   <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
            //     <FontAwesomeIcon icon="save" />
            //     &nbsp; Save
            //   </Button>
            // </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ExtraUserUpdate;

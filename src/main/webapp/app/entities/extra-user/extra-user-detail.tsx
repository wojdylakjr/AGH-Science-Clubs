import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './extra-user.reducer';

export const ExtraUserDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const extraUserEntity = useAppSelector(state => state.extraUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="extraUserDetailsHeading">ExtraUser</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{extraUserEntity.id}</dd>
          <dt>
            <span id="block">Block</span>
          </dt>
          <dd>{extraUserEntity.block}</dd>
          <dt>
            <span id="field">Field</span>
          </dt>
          <dd>{extraUserEntity.field}</dd>
          <dt>User</dt>
          <dd>{extraUserEntity.user ? extraUserEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/extra-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/extra-user/${extraUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExtraUserDetail;

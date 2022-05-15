import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IExtraUser } from 'app/shared/model/extra-user.model';
import { getEntities } from './extra-user.reducer';

export const ExtraUser = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const extraUserList = useAppSelector(state => state.extraUser.entities);
  const loading = useAppSelector(state => state.extraUser.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="extra-user-heading" data-cy="ExtraUserHeading">
        Extra Users
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/extra-user/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Extra User
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {extraUserList && extraUserList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Login</th>
                <th>Email</th>
                <th>Block</th>
                <th>Field</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {extraUserList.map((extraUser, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/extra-user/${extraUser.id}`} color="link" size="sm">
                      {extraUser.id}
                    </Button>
                  </td>
                  <td>{extraUser.user ? extraUser.user.login : ''}</td>
                  <td>{extraUser.user ? extraUser.user.email : ''}</td>
                  <td>{extraUser.block}</td>
                  <td>{extraUser.field}</td>
                  <td>{extraUser.user ? extraUser.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/extra-user/${extraUser.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/extra-user/${extraUser.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/extra-user/${extraUser.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Extra Users found</div>
        )}
      </div>
    </div>
  );
};

export default ExtraUser;

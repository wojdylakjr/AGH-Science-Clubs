import thunk from 'redux-thunk';
import axios from 'axios';
import sinon from 'sinon';
import configureStore from 'redux-mock-store';

import register, { handleRegister, reset } from './register.reducer';
import { Fields } from 'app/shared/model/enumerations/fields.model';
import { Blocks } from 'app/shared/model/enumerations/blocks.model';

describe('Creating account tests', () => {
  const initialState = {
    loading: false,
    registrationSuccess: false,
    registrationFailure: false,
    errorMessage: null,
    successMessage: null,
  };

  it('should return the initial state', () => {
    expect(register(undefined, { type: '' })).toEqual({
      ...initialState,
    });
  });

  it('should detect a request', () => {
    expect(register(undefined, { type: handleRegister.pending.type })).toEqual({
      ...initialState,
      loading: true,
    });
  });

  it('should handle RESET', () => {
    expect(
      register({ loading: true, registrationSuccess: true, registrationFailure: true, errorMessage: '', successMessage: '' }, reset())
    ).toEqual({
      ...initialState,
    });
  });

  it('should handle CREATE_ACCOUNT success', () => {
    expect(
      register(undefined, {
        type: handleRegister.fulfilled.type,
        payload: 'fake payload',
      })
    ).toEqual({
      ...initialState,
      registrationSuccess: true,
      successMessage: 'Registration saved! Please check your email for confirmation.',
    });
  });

  it('should handle CREATE_ACCOUNT failure', () => {
    const error = { message: 'fake error' };
    expect(
      register(undefined, {
        type: handleRegister.rejected.type,
        error,
      })
    ).toEqual({
      ...initialState,
      registrationFailure: true,
      errorMessage: error.message,
    });
  });

  describe('Actions', () => {
    let store;

    const resolvedObject = { value: 'whatever' };
    beforeEach(() => {
      const mockStore = configureStore([thunk]);
      store = mockStore({});
      axios.post = sinon.stub().returns(Promise.resolve(resolvedObject));
    });

    it('dispatches CREATE_ACCOUNT_PENDING and CREATE_ACCOUNT_FULFILLED actions', async () => {
      const expectedActions = [
        {
          type: handleRegister.pending.type,
        },
        {
          type: handleRegister.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(handleRegister({ login: '', email: '', password: '', block: Blocks.FIZYCZNY, field: Fields.CZARNY }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });
    it('dispatches RESET actions', async () => {
      await store.dispatch(reset());
      expect(store.getActions()[0]).toMatchObject(reset());
    });
  });
});
import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserAcquisition, defaultValue } from 'app/shared/model/user-acquisition.model';

export const ACTION_TYPES = {
  FETCH_USERACQUISITION_LIST: 'userAcquisition/FETCH_USERACQUISITION_LIST',
  FETCH_USERACQUISITION: 'userAcquisition/FETCH_USERACQUISITION',
  CREATE_USERACQUISITION: 'userAcquisition/CREATE_USERACQUISITION',
  UPDATE_USERACQUISITION: 'userAcquisition/UPDATE_USERACQUISITION',
  DELETE_USERACQUISITION: 'userAcquisition/DELETE_USERACQUISITION',
  RESET: 'userAcquisition/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserAcquisition>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type UserAcquisitionState = Readonly<typeof initialState>;

// Reducer

export default (state: UserAcquisitionState = initialState, action): UserAcquisitionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERACQUISITION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERACQUISITION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERACQUISITION):
    case REQUEST(ACTION_TYPES.UPDATE_USERACQUISITION):
    case REQUEST(ACTION_TYPES.DELETE_USERACQUISITION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_USERACQUISITION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERACQUISITION):
    case FAILURE(ACTION_TYPES.CREATE_USERACQUISITION):
    case FAILURE(ACTION_TYPES.UPDATE_USERACQUISITION):
    case FAILURE(ACTION_TYPES.DELETE_USERACQUISITION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERACQUISITION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERACQUISITION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERACQUISITION):
    case SUCCESS(ACTION_TYPES.UPDATE_USERACQUISITION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERACQUISITION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/user-acquisitions';

// Actions

export const getEntities: ICrudGetAllAction<IUserAcquisition> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_USERACQUISITION_LIST,
  payload: axios.get<IUserAcquisition>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IUserAcquisition> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERACQUISITION,
    payload: axios.get<IUserAcquisition>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserAcquisition> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERACQUISITION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserAcquisition> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERACQUISITION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserAcquisition> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERACQUISITION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

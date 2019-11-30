import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRevenue, defaultValue } from 'app/shared/model/revenue.model';

export const ACTION_TYPES = {
  FETCH_REVENUE_LIST: 'revenue/FETCH_REVENUE_LIST',
  FETCH_REVENUE: 'revenue/FETCH_REVENUE',
  CREATE_REVENUE: 'revenue/CREATE_REVENUE',
  UPDATE_REVENUE: 'revenue/UPDATE_REVENUE',
  DELETE_REVENUE: 'revenue/DELETE_REVENUE',
  RESET: 'revenue/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRevenue>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RevenueState = Readonly<typeof initialState>;

// Reducer

export default (state: RevenueState = initialState, action): RevenueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_REVENUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REVENUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_REVENUE):
    case REQUEST(ACTION_TYPES.UPDATE_REVENUE):
    case REQUEST(ACTION_TYPES.DELETE_REVENUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_REVENUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_REVENUE):
    case FAILURE(ACTION_TYPES.CREATE_REVENUE):
    case FAILURE(ACTION_TYPES.UPDATE_REVENUE):
    case FAILURE(ACTION_TYPES.DELETE_REVENUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_REVENUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_REVENUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_REVENUE):
    case SUCCESS(ACTION_TYPES.UPDATE_REVENUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_REVENUE):
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

const apiUrl = 'api/revenues';

// Actions

export const getEntities: ICrudGetAllAction<IRevenue> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_REVENUE_LIST,
  payload: axios.get<IRevenue>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRevenue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_REVENUE,
    payload: axios.get<IRevenue>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRevenue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_REVENUE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRevenue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_REVENUE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRevenue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_REVENUE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

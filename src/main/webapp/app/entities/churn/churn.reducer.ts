import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IChurn, defaultValue } from 'app/shared/model/churn.model';

export const ACTION_TYPES = {
  FETCH_CHURN_LIST: 'churn/FETCH_CHURN_LIST',
  FETCH_CHURN: 'churn/FETCH_CHURN',
  CREATE_CHURN: 'churn/CREATE_CHURN',
  UPDATE_CHURN: 'churn/UPDATE_CHURN',
  DELETE_CHURN: 'churn/DELETE_CHURN',
  RESET: 'churn/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IChurn>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ChurnState = Readonly<typeof initialState>;

// Reducer

export default (state: ChurnState = initialState, action): ChurnState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CHURN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CHURN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CHURN):
    case REQUEST(ACTION_TYPES.UPDATE_CHURN):
    case REQUEST(ACTION_TYPES.DELETE_CHURN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CHURN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CHURN):
    case FAILURE(ACTION_TYPES.CREATE_CHURN):
    case FAILURE(ACTION_TYPES.UPDATE_CHURN):
    case FAILURE(ACTION_TYPES.DELETE_CHURN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHURN_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHURN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CHURN):
    case SUCCESS(ACTION_TYPES.UPDATE_CHURN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CHURN):
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

const apiUrl = 'api/churns';

// Actions

export const getEntities: ICrudGetAllAction<IChurn> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CHURN_LIST,
  payload: axios.get<IChurn>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IChurn> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CHURN,
    payload: axios.get<IChurn>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IChurn> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CHURN,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IChurn> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CHURN,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IChurn> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CHURN,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

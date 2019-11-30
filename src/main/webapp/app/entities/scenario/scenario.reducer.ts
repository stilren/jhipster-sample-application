import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IScenario, defaultValue } from 'app/shared/model/scenario.model';

export const ACTION_TYPES = {
  FETCH_SCENARIO_LIST: 'scenario/FETCH_SCENARIO_LIST',
  FETCH_SCENARIO: 'scenario/FETCH_SCENARIO',
  CREATE_SCENARIO: 'scenario/CREATE_SCENARIO',
  UPDATE_SCENARIO: 'scenario/UPDATE_SCENARIO',
  DELETE_SCENARIO: 'scenario/DELETE_SCENARIO',
  RESET: 'scenario/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IScenario>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ScenarioState = Readonly<typeof initialState>;

// Reducer

export default (state: ScenarioState = initialState, action): ScenarioState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SCENARIO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SCENARIO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SCENARIO):
    case REQUEST(ACTION_TYPES.UPDATE_SCENARIO):
    case REQUEST(ACTION_TYPES.DELETE_SCENARIO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SCENARIO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SCENARIO):
    case FAILURE(ACTION_TYPES.CREATE_SCENARIO):
    case FAILURE(ACTION_TYPES.UPDATE_SCENARIO):
    case FAILURE(ACTION_TYPES.DELETE_SCENARIO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCENARIO_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_SCENARIO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SCENARIO):
    case SUCCESS(ACTION_TYPES.UPDATE_SCENARIO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SCENARIO):
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

const apiUrl = 'api/scenarios';

// Actions

export const getEntities: ICrudGetAllAction<IScenario> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SCENARIO_LIST,
    payload: axios.get<IScenario>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IScenario> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SCENARIO,
    payload: axios.get<IScenario>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IScenario> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SCENARIO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IScenario> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SCENARIO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IScenario> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SCENARIO,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

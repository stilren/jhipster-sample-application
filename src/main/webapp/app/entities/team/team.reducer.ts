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

import { ITeam, defaultValue } from 'app/shared/model/team.model';

export const ACTION_TYPES = {
  FETCH_TEAM_LIST: 'team/FETCH_TEAM_LIST',
  FETCH_TEAM: 'team/FETCH_TEAM',
  CREATE_TEAM: 'team/CREATE_TEAM',
  UPDATE_TEAM: 'team/UPDATE_TEAM',
  DELETE_TEAM: 'team/DELETE_TEAM',
  RESET: 'team/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITeam>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type TeamState = Readonly<typeof initialState>;

// Reducer

export default (state: TeamState = initialState, action): TeamState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TEAM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEAM):
    case REQUEST(ACTION_TYPES.UPDATE_TEAM):
    case REQUEST(ACTION_TYPES.DELETE_TEAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TEAM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEAM):
    case FAILURE(ACTION_TYPES.CREATE_TEAM):
    case FAILURE(ACTION_TYPES.UPDATE_TEAM):
    case FAILURE(ACTION_TYPES.DELETE_TEAM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEAM_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_TEAM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEAM):
    case SUCCESS(ACTION_TYPES.UPDATE_TEAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEAM):
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

const apiUrl = 'api/teams';

// Actions

export const getEntities: ICrudGetAllAction<ITeam> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TEAM_LIST,
    payload: axios.get<ITeam>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ITeam> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEAM,
    payload: axios.get<ITeam>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITeam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEAM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<ITeam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEAM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITeam> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEAM,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

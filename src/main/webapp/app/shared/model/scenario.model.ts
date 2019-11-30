import { ITeam } from 'app/shared/model/team.model';
import { ICohort } from 'app/shared/model/cohort.model';

export interface IScenario {
  id?: number;
  scenarioName?: string;
  team?: ITeam;
  cohorts?: ICohort[];
}

export const defaultValue: Readonly<IScenario> = {};

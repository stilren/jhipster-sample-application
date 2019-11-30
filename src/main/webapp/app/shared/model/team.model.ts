import { IScenario } from 'app/shared/model/scenario.model';
import { ICohort } from 'app/shared/model/cohort.model';

export interface ITeam {
  id?: number;
  teamName?: string;
  scenarios?: IScenario[];
  cohorts?: ICohort[];
}

export const defaultValue: Readonly<ITeam> = {};

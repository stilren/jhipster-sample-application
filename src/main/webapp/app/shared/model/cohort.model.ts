import { IUserAcquisition } from 'app/shared/model/user-acquisition.model';
import { IChurn } from 'app/shared/model/churn.model';
import { IRevenue } from 'app/shared/model/revenue.model';
import { IScenario } from 'app/shared/model/scenario.model';
import { ITeam } from 'app/shared/model/team.model';

export interface ICohort {
  id?: number;
  cohortName?: string;
  userAcquisition?: IUserAcquisition;
  churn?: IChurn;
  revenue?: IRevenue;
  scenarios?: IScenario[];
  team?: ITeam;
}

export const defaultValue: Readonly<ICohort> = {};

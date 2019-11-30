import { ICohort } from 'app/shared/model/cohort.model';

export interface IChurn {
  id?: number;
  chrunPerTick?: string;
  cronSchedule?: string;
  delay?: number;
  cohort?: ICohort;
}

export const defaultValue: Readonly<IChurn> = {};

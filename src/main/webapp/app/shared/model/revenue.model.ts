import { ICohort } from 'app/shared/model/cohort.model';

export interface IRevenue {
  id?: number;
  revenuePerTick?: string;
  cronSchedule?: string;
  delay?: number;
  cohort?: ICohort;
}

export const defaultValue: Readonly<IRevenue> = {};

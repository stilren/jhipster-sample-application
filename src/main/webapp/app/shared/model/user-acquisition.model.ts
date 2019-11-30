import { ICohort } from 'app/shared/model/cohort.model';

export interface IUserAcquisition {
  id?: number;
  usersAcquiredPerTick?: string;
  cronSchedule?: string;
  repeat?: number;
  delay?: number;
  cohort?: ICohort;
}

export const defaultValue: Readonly<IUserAcquisition> = {};

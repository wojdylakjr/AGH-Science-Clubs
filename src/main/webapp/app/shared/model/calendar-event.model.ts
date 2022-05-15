import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ICalendarEvent {
  id?: number;
  title?: string;
  startDate?: string | null;
  endDate?: string | null;
  publicationDate?: string | null;
  description?: string | null;
  link?: string | null;
  imageUrl?: string | null;
  status?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ICalendarEvent> = {};

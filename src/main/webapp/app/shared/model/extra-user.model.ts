import { IUser } from 'app/shared/model/user.model';
import { Blocks } from 'app/shared/model/enumerations/blocks.model';
import { Fields } from 'app/shared/model/enumerations/fields.model';

export interface IExtraUser {
  id?: number;
  block?: Blocks | null;
  field?: Fields | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IExtraUser> = {};

import { IUserPermissions } from 'app/shared/model/user-permissions.model';
import { IExUser } from 'app/shared/model/ex-user.model';

export interface IUserGroup {
  id?: number;
  groupName?: string;
  userPermissions?: IUserPermissions[];
  users?: IExUser[];
}

export class UserGroup implements IUserGroup {
  constructor(public id?: number, public groupName?: string, public userPermissions?: IUserPermissions[], public users?: IExUser[]) {}
}

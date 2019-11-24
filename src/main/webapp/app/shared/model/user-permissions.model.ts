import { IMenuItems } from 'app/shared/model/menu-items.model';
import { IExUser } from 'app/shared/model/ex-user.model';
import { IUserGroups } from 'app/shared/model/user-groups.model';

export interface IUserPermissions {
  id?: number;
  userPermKey?: string;
  userPermDescription?: string;
  isActive?: boolean;
  menuItems?: IMenuItems[];
  userPermissions?: IExUser[];
  userGroups?: IUserGroups[];
}

export class UserPermissions implements IUserPermissions {
  constructor(
    public id?: number,
    public userPermKey?: string,
    public userPermDescription?: string,
    public isActive?: boolean,
    public menuItems?: IMenuItems[],
    public userPermissions?: IExUser[],
    public userGroups?: IUserGroups[]
  ) {
    this.isActive = this.isActive || false;
  }
}

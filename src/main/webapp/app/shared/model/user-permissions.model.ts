import { IExUser } from 'app/shared/model/ex-user.model';
import { IMenuItems } from 'app/shared/model/menu-items.model';
import { IUserGroup } from 'app/shared/model/user-group.model';

export interface IUserPermissions {
  id?: number;
  userPermKey?: string;
  userPermDescription?: string;
  isActive?: boolean;
  user?: IExUser;
  menuItems?: IMenuItems[];
  userGroup?: IUserGroup;
}

export class UserPermissions implements IUserPermissions {
  constructor(
    public id?: number,
    public userPermKey?: string,
    public userPermDescription?: string,
    public isActive?: boolean,
    public user?: IExUser,
    public menuItems?: IMenuItems[],
    public userGroup?: IUserGroup
  ) {
    this.isActive = this.isActive || false;
  }
}

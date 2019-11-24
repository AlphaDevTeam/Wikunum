import { IUser } from 'app/core/user/user.model';
import { ICompany } from 'app/shared/model/company.model';
import { ILocation } from 'app/shared/model/location.model';
import { IUserGroups } from 'app/shared/model/user-groups.model';
import { IUserPermissions } from 'app/shared/model/user-permissions.model';

export interface IExUser {
  id?: number;
  userKey?: string;
  relatedUser?: IUser;
  company?: ICompany;
  locations?: ILocation[];
  userGroups?: IUserGroups[];
  userPermissions?: IUserPermissions[];
}

export class ExUser implements IExUser {
  constructor(
    public id?: number,
    public userKey?: string,
    public relatedUser?: IUser,
    public company?: ICompany,
    public locations?: ILocation[],
    public userGroups?: IUserGroups[],
    public userPermissions?: IUserPermissions[]
  ) {}
}

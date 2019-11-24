import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserGroups } from 'app/shared/model/user-groups.model';
import { UserGroupsService } from './user-groups.service';
import { UserGroupsComponent } from './user-groups.component';
import { UserGroupsDetailComponent } from './user-groups-detail.component';
import { UserGroupsUpdateComponent } from './user-groups-update.component';
import { IUserGroups } from 'app/shared/model/user-groups.model';

@Injectable({ providedIn: 'root' })
export class UserGroupsResolve implements Resolve<IUserGroups> {
  constructor(private service: UserGroupsService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserGroups> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((userGroups: HttpResponse<UserGroups>) => userGroups.body));
    }
    return of(new UserGroups());
  }
}

export const userGroupsRoute: Routes = [
  {
    path: '',
    component: UserGroupsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserGroups'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserGroupsDetailComponent,
    resolve: {
      userGroups: UserGroupsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserGroups'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserGroupsUpdateComponent,
    resolve: {
      userGroups: UserGroupsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserGroups'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserGroupsUpdateComponent,
    resolve: {
      userGroups: UserGroupsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserGroups'
    },
    canActivate: [UserRouteAccessService]
  }
];

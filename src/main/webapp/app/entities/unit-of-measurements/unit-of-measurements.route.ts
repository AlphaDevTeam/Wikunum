import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UnitOfMeasurements } from 'app/shared/model/unit-of-measurements.model';
import { UnitOfMeasurementsService } from './unit-of-measurements.service';
import { UnitOfMeasurementsComponent } from './unit-of-measurements.component';
import { UnitOfMeasurementsDetailComponent } from './unit-of-measurements-detail.component';
import { UnitOfMeasurementsUpdateComponent } from './unit-of-measurements-update.component';
import { IUnitOfMeasurements } from 'app/shared/model/unit-of-measurements.model';

@Injectable({ providedIn: 'root' })
export class UnitOfMeasurementsResolve implements Resolve<IUnitOfMeasurements> {
  constructor(private service: UnitOfMeasurementsService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUnitOfMeasurements> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((unitOfMeasurements: HttpResponse<UnitOfMeasurements>) => unitOfMeasurements.body));
    }
    return of(new UnitOfMeasurements());
  }
}

export const unitOfMeasurementsRoute: Routes = [
  {
    path: '',
    component: UnitOfMeasurementsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasurements'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UnitOfMeasurementsDetailComponent,
    resolve: {
      unitOfMeasurements: UnitOfMeasurementsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasurements'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UnitOfMeasurementsUpdateComponent,
    resolve: {
      unitOfMeasurements: UnitOfMeasurementsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasurements'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UnitOfMeasurementsUpdateComponent,
    resolve: {
      unitOfMeasurements: UnitOfMeasurementsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasurements'
    },
    canActivate: [UserRouteAccessService]
  }
];

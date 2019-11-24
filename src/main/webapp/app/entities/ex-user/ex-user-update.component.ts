import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IExUser, ExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from './ex-user.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company/company.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { IUserGroups } from 'app/shared/model/user-groups.model';
import { UserGroupsService } from 'app/entities/user-groups/user-groups.service';
import { IUserPermissions } from 'app/shared/model/user-permissions.model';
import { UserPermissionsService } from 'app/entities/user-permissions/user-permissions.service';

@Component({
  selector: 'jhi-ex-user-update',
  templateUrl: './ex-user-update.component.html'
})
export class ExUserUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  companies: ICompany[];

  locations: ILocation[];

  usergroups: IUserGroups[];

  userpermissions: IUserPermissions[];

  editForm = this.fb.group({
    id: [],
    userKey: [null, [Validators.required]],
    relatedUser: [],
    company: [null, Validators.required],
    locations: [null, Validators.required],
    userGroups: [],
    userPermissions: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected exUserService: ExUserService,
    protected userService: UserService,
    protected companyService: CompanyService,
    protected locationService: LocationService,
    protected userGroupsService: UserGroupsService,
    protected userPermissionsService: UserPermissionsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ exUser }) => {
      this.updateForm(exUser);
    });
    this.userService
      .query()
      .subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.companyService.query({ 'exUserId.specified': 'false' }).subscribe(
      (res: HttpResponse<ICompany[]>) => {
        if (!this.editForm.get('company').value || !this.editForm.get('company').value.id) {
          this.companies = res.body;
        } else {
          this.companyService
            .find(this.editForm.get('company').value.id)
            .subscribe(
              (subRes: HttpResponse<ICompany>) => (this.companies = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.locationService
      .query()
      .subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.userGroupsService
      .query()
      .subscribe((res: HttpResponse<IUserGroups[]>) => (this.usergroups = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.userPermissionsService
      .query()
      .subscribe(
        (res: HttpResponse<IUserPermissions[]>) => (this.userpermissions = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(exUser: IExUser) {
    this.editForm.patchValue({
      id: exUser.id,
      userKey: exUser.userKey,
      relatedUser: exUser.relatedUser,
      company: exUser.company,
      locations: exUser.locations,
      userGroups: exUser.userGroups,
      userPermissions: exUser.userPermissions
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const exUser = this.createFromForm();
    if (exUser.id !== undefined) {
      this.subscribeToSaveResponse(this.exUserService.update(exUser));
    } else {
      this.subscribeToSaveResponse(this.exUserService.create(exUser));
    }
  }

  private createFromForm(): IExUser {
    return {
      ...new ExUser(),
      id: this.editForm.get(['id']).value,
      userKey: this.editForm.get(['userKey']).value,
      relatedUser: this.editForm.get(['relatedUser']).value,
      company: this.editForm.get(['company']).value,
      locations: this.editForm.get(['locations']).value,
      userGroups: this.editForm.get(['userGroups']).value,
      userPermissions: this.editForm.get(['userPermissions']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExUser>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackCompanyById(index: number, item: ICompany) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }

  trackUserGroupsById(index: number, item: IUserGroups) {
    return item.id;
  }

  trackUserPermissionsById(index: number, item: IUserPermissions) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}

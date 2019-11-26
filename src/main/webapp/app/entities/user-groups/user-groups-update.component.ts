import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IUserGroups, UserGroups } from 'app/shared/model/user-groups.model';
import { UserGroupsService } from './user-groups.service';
import { IUserPermissions } from 'app/shared/model/user-permissions.model';
import { UserPermissionsService } from 'app/entities/user-permissions/user-permissions.service';
import { IExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/ex-user.service';

@Component({
  selector: 'jhi-user-groups-update',
  templateUrl: './user-groups-update.component.html'
})
export class UserGroupsUpdateComponent implements OnInit {
  isSaving: boolean;

  userpermissions: IUserPermissions[];

  exusers: IExUser[];

  editForm = this.fb.group({
    id: [],
    groupName: [null, [Validators.required]],
    userPermissions: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userGroupsService: UserGroupsService,
    protected userPermissionsService: UserPermissionsService,
    protected exUserService: ExUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userGroups }) => {
      this.updateForm(userGroups);
    });
    this.userPermissionsService
      .query()
      .subscribe(
        (res: HttpResponse<IUserPermissions[]>) => (this.userpermissions = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.exUserService
      .query()
      .subscribe((res: HttpResponse<IExUser[]>) => (this.exusers = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userGroups: IUserGroups) {
    this.editForm.patchValue({
      id: userGroups.id,
      groupName: userGroups.groupName,
      userPermissions: userGroups.userPermissions
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userGroups = this.createFromForm();
    if (userGroups.id !== undefined) {
      this.subscribeToSaveResponse(this.userGroupsService.update(userGroups));
    } else {
      this.subscribeToSaveResponse(this.userGroupsService.create(userGroups));
    }
  }

  private createFromForm(): IUserGroups {
    return {
      ...new UserGroups(),
      id: this.editForm.get(['id']).value,
      groupName: this.editForm.get(['groupName']).value,
      userPermissions: this.editForm.get(['userPermissions']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserGroups>>) {
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

  trackUserPermissionsById(index: number, item: IUserPermissions) {
    return item.id;
  }

  trackExUserById(index: number, item: IExUser) {
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

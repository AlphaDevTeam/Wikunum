import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IUserPermissions, UserPermissions } from 'app/shared/model/user-permissions.model';
import { UserPermissionsService } from './user-permissions.service';
import { IMenuItems } from 'app/shared/model/menu-items.model';
import { MenuItemsService } from 'app/entities/menu-items/menu-items.service';
import { IExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/ex-user.service';
import { IUserGroups } from 'app/shared/model/user-groups.model';
import { UserGroupsService } from 'app/entities/user-groups/user-groups.service';

@Component({
  selector: 'jhi-user-permissions-update',
  templateUrl: './user-permissions-update.component.html'
})
export class UserPermissionsUpdateComponent implements OnInit {
  isSaving: boolean;

  menuitems: IMenuItems[];

  exusers: IExUser[];

  usergroups: IUserGroups[];

  editForm = this.fb.group({
    id: [],
    userPermKey: [],
    userPermDescription: [],
    isActive: [],
    menuItems: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userPermissionsService: UserPermissionsService,
    protected menuItemsService: MenuItemsService,
    protected exUserService: ExUserService,
    protected userGroupsService: UserGroupsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userPermissions }) => {
      this.updateForm(userPermissions);
    });
    this.menuItemsService
      .query()
      .subscribe((res: HttpResponse<IMenuItems[]>) => (this.menuitems = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.exUserService
      .query()
      .subscribe((res: HttpResponse<IExUser[]>) => (this.exusers = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.userGroupsService
      .query()
      .subscribe((res: HttpResponse<IUserGroups[]>) => (this.usergroups = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userPermissions: IUserPermissions) {
    this.editForm.patchValue({
      id: userPermissions.id,
      userPermKey: userPermissions.userPermKey,
      userPermDescription: userPermissions.userPermDescription,
      isActive: userPermissions.isActive,
      menuItems: userPermissions.menuItems
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userPermissions = this.createFromForm();
    if (userPermissions.id !== undefined) {
      this.subscribeToSaveResponse(this.userPermissionsService.update(userPermissions));
    } else {
      this.subscribeToSaveResponse(this.userPermissionsService.create(userPermissions));
    }
  }

  private createFromForm(): IUserPermissions {
    return {
      ...new UserPermissions(),
      id: this.editForm.get(['id']).value,
      userPermKey: this.editForm.get(['userPermKey']).value,
      userPermDescription: this.editForm.get(['userPermDescription']).value,
      isActive: this.editForm.get(['isActive']).value,
      menuItems: this.editForm.get(['menuItems']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserPermissions>>) {
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

  trackMenuItemsById(index: number, item: IMenuItems) {
    return item.id;
  }

  trackExUserById(index: number, item: IExUser) {
    return item.id;
  }

  trackUserGroupsById(index: number, item: IUserGroups) {
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

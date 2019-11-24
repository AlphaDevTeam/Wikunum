import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IMenuItems, MenuItems } from 'app/shared/model/menu-items.model';
import { MenuItemsService } from './menu-items.service';
import { IUserPermissions } from 'app/shared/model/user-permissions.model';
import { UserPermissionsService } from 'app/entities/user-permissions/user-permissions.service';

@Component({
  selector: 'jhi-menu-items-update',
  templateUrl: './menu-items-update.component.html'
})
export class MenuItemsUpdateComponent implements OnInit {
  isSaving: boolean;

  userpermissions: IUserPermissions[];

  editForm = this.fb.group({
    id: [],
    menuName: [null, [Validators.required]],
    menuURL: [null, [Validators.required]],
    isActive: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected menuItemsService: MenuItemsService,
    protected userPermissionsService: UserPermissionsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ menuItems }) => {
      this.updateForm(menuItems);
    });
    this.userPermissionsService
      .query()
      .subscribe(
        (res: HttpResponse<IUserPermissions[]>) => (this.userpermissions = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(menuItems: IMenuItems) {
    this.editForm.patchValue({
      id: menuItems.id,
      menuName: menuItems.menuName,
      menuURL: menuItems.menuURL,
      isActive: menuItems.isActive
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const menuItems = this.createFromForm();
    if (menuItems.id !== undefined) {
      this.subscribeToSaveResponse(this.menuItemsService.update(menuItems));
    } else {
      this.subscribeToSaveResponse(this.menuItemsService.create(menuItems));
    }
  }

  private createFromForm(): IMenuItems {
    return {
      ...new MenuItems(),
      id: this.editForm.get(['id']).value,
      menuName: this.editForm.get(['menuName']).value,
      menuURL: this.editForm.get(['menuURL']).value,
      isActive: this.editForm.get(['isActive']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMenuItems>>) {
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

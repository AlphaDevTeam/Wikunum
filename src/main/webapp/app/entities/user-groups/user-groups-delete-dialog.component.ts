import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserGroups } from 'app/shared/model/user-groups.model';
import { UserGroupsService } from './user-groups.service';

@Component({
  templateUrl: './user-groups-delete-dialog.component.html'
})
export class UserGroupsDeleteDialogComponent {
  userGroups: IUserGroups;

  constructor(
    protected userGroupsService: UserGroupsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.userGroupsService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'userGroupsListModification',
        content: 'Deleted an userGroups'
      });
      this.activeModal.dismiss(true);
    });
  }
}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { UserGroupsComponent } from './user-groups.component';
import { UserGroupsDetailComponent } from './user-groups-detail.component';
import { UserGroupsUpdateComponent } from './user-groups-update.component';
import { UserGroupsDeleteDialogComponent } from './user-groups-delete-dialog.component';
import { userGroupsRoute } from './user-groups.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(userGroupsRoute)],
  declarations: [UserGroupsComponent, UserGroupsDetailComponent, UserGroupsUpdateComponent, UserGroupsDeleteDialogComponent],
  entryComponents: [UserGroupsDeleteDialogComponent]
})
export class WikunumUserGroupsModule {}

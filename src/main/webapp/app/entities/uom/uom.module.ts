import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { UOMComponent } from './uom.component';
import { UOMDetailComponent } from './uom-detail.component';
import { UOMUpdateComponent } from './uom-update.component';
import { UOMDeleteDialogComponent } from './uom-delete-dialog.component';
import { uOMRoute } from './uom.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(uOMRoute)],
  declarations: [UOMComponent, UOMDetailComponent, UOMUpdateComponent, UOMDeleteDialogComponent],
  entryComponents: [UOMDeleteDialogComponent]
})
export class WikunumUOMModule {}

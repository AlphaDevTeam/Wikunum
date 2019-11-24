import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { UnitOfMeasurementsComponent } from './unit-of-measurements.component';
import { UnitOfMeasurementsDetailComponent } from './unit-of-measurements-detail.component';
import { UnitOfMeasurementsUpdateComponent } from './unit-of-measurements-update.component';
import { UnitOfMeasurementsDeleteDialogComponent } from './unit-of-measurements-delete-dialog.component';
import { unitOfMeasurementsRoute } from './unit-of-measurements.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(unitOfMeasurementsRoute)],
  declarations: [
    UnitOfMeasurementsComponent,
    UnitOfMeasurementsDetailComponent,
    UnitOfMeasurementsUpdateComponent,
    UnitOfMeasurementsDeleteDialogComponent
  ],
  entryComponents: [UnitOfMeasurementsDeleteDialogComponent]
})
export class WikunumUnitOfMeasurementsModule {}

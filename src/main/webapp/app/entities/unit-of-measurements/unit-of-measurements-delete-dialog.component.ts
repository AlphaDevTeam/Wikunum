import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUnitOfMeasurements } from 'app/shared/model/unit-of-measurements.model';
import { UnitOfMeasurementsService } from './unit-of-measurements.service';

@Component({
  templateUrl: './unit-of-measurements-delete-dialog.component.html'
})
export class UnitOfMeasurementsDeleteDialogComponent {
  unitOfMeasurements: IUnitOfMeasurements;

  constructor(
    protected unitOfMeasurementsService: UnitOfMeasurementsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.unitOfMeasurementsService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'unitOfMeasurementsListModification',
        content: 'Deleted an unitOfMeasurements'
      });
      this.activeModal.dismiss(true);
    });
  }
}

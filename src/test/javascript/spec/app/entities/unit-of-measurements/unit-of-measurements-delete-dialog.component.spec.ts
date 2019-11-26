import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { UnitOfMeasurementsDeleteDialogComponent } from 'app/entities/unit-of-measurements/unit-of-measurements-delete-dialog.component';
import { UnitOfMeasurementsService } from 'app/entities/unit-of-measurements/unit-of-measurements.service';

describe('Component Tests', () => {
  describe('UnitOfMeasurements Management Delete Component', () => {
    let comp: UnitOfMeasurementsDeleteDialogComponent;
    let fixture: ComponentFixture<UnitOfMeasurementsDeleteDialogComponent>;
    let service: UnitOfMeasurementsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [UnitOfMeasurementsDeleteDialogComponent]
      })
        .overrideTemplate(UnitOfMeasurementsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UnitOfMeasurementsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UnitOfMeasurementsService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});

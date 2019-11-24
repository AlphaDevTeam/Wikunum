import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { UnitOfMeasurementsUpdateComponent } from 'app/entities/unit-of-measurements/unit-of-measurements-update.component';
import { UnitOfMeasurementsService } from 'app/entities/unit-of-measurements/unit-of-measurements.service';
import { UnitOfMeasurements } from 'app/shared/model/unit-of-measurements.model';

describe('Component Tests', () => {
  describe('UnitOfMeasurements Management Update Component', () => {
    let comp: UnitOfMeasurementsUpdateComponent;
    let fixture: ComponentFixture<UnitOfMeasurementsUpdateComponent>;
    let service: UnitOfMeasurementsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [UnitOfMeasurementsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UnitOfMeasurementsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UnitOfMeasurementsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UnitOfMeasurementsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UnitOfMeasurements(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new UnitOfMeasurements();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

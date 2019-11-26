import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { UnitOfMeasurementsDetailComponent } from 'app/entities/unit-of-measurements/unit-of-measurements-detail.component';
import { UnitOfMeasurements } from 'app/shared/model/unit-of-measurements.model';

describe('Component Tests', () => {
  describe('UnitOfMeasurements Management Detail Component', () => {
    let comp: UnitOfMeasurementsDetailComponent;
    let fixture: ComponentFixture<UnitOfMeasurementsDetailComponent>;
    const route = ({ data: of({ unitOfMeasurements: new UnitOfMeasurements(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [UnitOfMeasurementsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UnitOfMeasurementsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UnitOfMeasurementsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.unitOfMeasurements).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

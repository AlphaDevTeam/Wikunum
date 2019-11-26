import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUnitOfMeasurements } from 'app/shared/model/unit-of-measurements.model';

@Component({
  selector: 'jhi-unit-of-measurements-detail',
  templateUrl: './unit-of-measurements-detail.component.html'
})
export class UnitOfMeasurementsDetailComponent implements OnInit {
  unitOfMeasurements: IUnitOfMeasurements;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ unitOfMeasurements }) => {
      this.unitOfMeasurements = unitOfMeasurements;
    });
  }

  previousState() {
    window.history.back();
  }
}

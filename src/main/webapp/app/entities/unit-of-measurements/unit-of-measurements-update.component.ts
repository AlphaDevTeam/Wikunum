import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IUnitOfMeasurements, UnitOfMeasurements } from 'app/shared/model/unit-of-measurements.model';
import { UnitOfMeasurementsService } from './unit-of-measurements.service';

@Component({
  selector: 'jhi-unit-of-measurements-update',
  templateUrl: './unit-of-measurements-update.component.html'
})
export class UnitOfMeasurementsUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    uomCode: [null, [Validators.required]],
    uomDescription: [null, [Validators.required]]
  });

  constructor(
    protected unitOfMeasurementsService: UnitOfMeasurementsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ unitOfMeasurements }) => {
      this.updateForm(unitOfMeasurements);
    });
  }

  updateForm(unitOfMeasurements: IUnitOfMeasurements) {
    this.editForm.patchValue({
      id: unitOfMeasurements.id,
      uomCode: unitOfMeasurements.uomCode,
      uomDescription: unitOfMeasurements.uomDescription
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const unitOfMeasurements = this.createFromForm();
    if (unitOfMeasurements.id !== undefined) {
      this.subscribeToSaveResponse(this.unitOfMeasurementsService.update(unitOfMeasurements));
    } else {
      this.subscribeToSaveResponse(this.unitOfMeasurementsService.create(unitOfMeasurements));
    }
  }

  private createFromForm(): IUnitOfMeasurements {
    return {
      ...new UnitOfMeasurements(),
      id: this.editForm.get(['id']).value,
      uomCode: this.editForm.get(['uomCode']).value,
      uomDescription: this.editForm.get(['uomDescription']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUnitOfMeasurements>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}

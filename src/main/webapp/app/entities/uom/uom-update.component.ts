import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IUOM, UOM } from 'app/shared/model/uom.model';
import { UOMService } from './uom.service';

@Component({
  selector: 'jhi-uom-update',
  templateUrl: './uom-update.component.html'
})
export class UOMUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    uomCode: [null, [Validators.required]],
    uomDescription: [null, [Validators.required]]
  });

  constructor(protected uOMService: UOMService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ uOM }) => {
      this.updateForm(uOM);
    });
  }

  updateForm(uOM: IUOM) {
    this.editForm.patchValue({
      id: uOM.id,
      uomCode: uOM.uomCode,
      uomDescription: uOM.uomDescription
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const uOM = this.createFromForm();
    if (uOM.id !== undefined) {
      this.subscribeToSaveResponse(this.uOMService.update(uOM));
    } else {
      this.subscribeToSaveResponse(this.uOMService.create(uOM));
    }
  }

  private createFromForm(): IUOM {
    return {
      ...new UOM(),
      id: this.editForm.get(['id']).value,
      uomCode: this.editForm.get(['uomCode']).value,
      uomDescription: this.editForm.get(['uomDescription']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUOM>>) {
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

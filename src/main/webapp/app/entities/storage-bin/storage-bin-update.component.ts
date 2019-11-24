import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IStorageBin, StorageBin } from 'app/shared/model/storage-bin.model';
import { StorageBinService } from './storage-bin.service';

@Component({
  selector: 'jhi-storage-bin-update',
  templateUrl: './storage-bin-update.component.html'
})
export class StorageBinUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    binNumber: [null, [Validators.required]],
    binDescription: [null, [Validators.required]]
  });

  constructor(protected storageBinService: StorageBinService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ storageBin }) => {
      this.updateForm(storageBin);
    });
  }

  updateForm(storageBin: IStorageBin) {
    this.editForm.patchValue({
      id: storageBin.id,
      binNumber: storageBin.binNumber,
      binDescription: storageBin.binDescription
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const storageBin = this.createFromForm();
    if (storageBin.id !== undefined) {
      this.subscribeToSaveResponse(this.storageBinService.update(storageBin));
    } else {
      this.subscribeToSaveResponse(this.storageBinService.create(storageBin));
    }
  }

  private createFromForm(): IStorageBin {
    return {
      ...new StorageBin(),
      id: this.editForm.get(['id']).value,
      binNumber: this.editForm.get(['binNumber']).value,
      binDescription: this.editForm.get(['binDescription']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStorageBin>>) {
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

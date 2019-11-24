import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IWorker, Worker } from 'app/shared/model/worker.model';
import { WorkerService } from './worker.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { IJob } from 'app/shared/model/job.model';
import { JobService } from 'app/entities/job/job.service';

@Component({
  selector: 'jhi-worker-update',
  templateUrl: './worker-update.component.html'
})
export class WorkerUpdateComponent implements OnInit {
  isSaving: boolean;

  locations: ILocation[];

  jobs: IJob[];

  editForm = this.fb.group({
    id: [],
    workerCode: [null, [Validators.required]],
    workerName: [null, [Validators.required]],
    workerLimit: [],
    isActive: [],
    location: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected workerService: WorkerService,
    protected locationService: LocationService,
    protected jobService: JobService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ worker }) => {
      this.updateForm(worker);
    });
    this.locationService
      .query()
      .subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.jobService
      .query()
      .subscribe((res: HttpResponse<IJob[]>) => (this.jobs = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(worker: IWorker) {
    this.editForm.patchValue({
      id: worker.id,
      workerCode: worker.workerCode,
      workerName: worker.workerName,
      workerLimit: worker.workerLimit,
      isActive: worker.isActive,
      location: worker.location
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const worker = this.createFromForm();
    if (worker.id !== undefined) {
      this.subscribeToSaveResponse(this.workerService.update(worker));
    } else {
      this.subscribeToSaveResponse(this.workerService.create(worker));
    }
  }

  private createFromForm(): IWorker {
    return {
      ...new Worker(),
      id: this.editForm.get(['id']).value,
      workerCode: this.editForm.get(['workerCode']).value,
      workerName: this.editForm.get(['workerName']).value,
      workerLimit: this.editForm.get(['workerLimit']).value,
      isActive: this.editForm.get(['isActive']).value,
      location: this.editForm.get(['location']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorker>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }

  trackJobById(index: number, item: IJob) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUnitOfMeasurements } from 'app/shared/model/unit-of-measurements.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { UnitOfMeasurementsService } from './unit-of-measurements.service';
import { UnitOfMeasurementsDeleteDialogComponent } from './unit-of-measurements-delete-dialog.component';

@Component({
  selector: 'jhi-unit-of-measurements',
  templateUrl: './unit-of-measurements.component.html'
})
export class UnitOfMeasurementsComponent implements OnInit, OnDestroy {
  unitOfMeasurements: IUnitOfMeasurements[];
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;

  constructor(
    protected unitOfMeasurementsService: UnitOfMeasurementsService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.unitOfMeasurements = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
    this.unitOfMeasurementsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IUnitOfMeasurements[]>) => this.paginateUnitOfMeasurements(res.body, res.headers));
  }

  reset() {
    this.page = 0;
    this.unitOfMeasurements = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInUnitOfMeasurements();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IUnitOfMeasurements) {
    return item.id;
  }

  registerChangeInUnitOfMeasurements() {
    this.eventSubscriber = this.eventManager.subscribe('unitOfMeasurementsListModification', () => this.reset());
  }

  delete(unitOfMeasurements: IUnitOfMeasurements) {
    const modalRef = this.modalService.open(UnitOfMeasurementsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.unitOfMeasurements = unitOfMeasurements;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateUnitOfMeasurements(data: IUnitOfMeasurements[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.unitOfMeasurements.push(data[i]);
    }
  }
}

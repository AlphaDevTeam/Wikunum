import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators } from '@angular/forms';

import { ISupplierAccount } from 'app/shared/model/supplier-account.model';
import { JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SupplierAccountService } from './supplier-account.service';
import { SupplierAccountDeleteDialogComponent } from './supplier-account-delete-dialog.component';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier/supplier.service';

@Component({
  selector: 'jhi-supplier-account',
  templateUrl: './supplier-account.component.html'
})
export class SupplierAccountComponent implements OnInit, OnDestroy {
  locations: ILocation[];
  transactiontypes: ITransactionType[];
  suppliers: ISupplier[];

  supplierAccounts: ISupplierAccount[];
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;

  editForm = this.fb.group({
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    supplier: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected supplierAccountService: SupplierAccountService,
    protected eventManager: JhiEventManager,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected supplierService: SupplierService,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks,
    private fb: FormBuilder
  ) {
    this.supplierAccounts = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
    this.supplierAccountService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        'locationId.equals': this.editForm.get(['location']).value.id,
        'transactionTypeId.equals': this.editForm.get(['transactionType']).value.id,
        'supplierId.equals': this.editForm.get(['supplier']).value.id
      })
      .subscribe((res: HttpResponse<ISupplierAccount[]>) => this.paginateSupplierAccounts(res.body, res.headers));
  }

  reset() {
    this.page = 0;
    this.supplierAccounts = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  search() {
    this.supplierAccounts = []; // reset form
    this.predicate = 'id'; // reset Sort
    this.loadAll();
    this.registerChangeInSupplierAccounts();
  }

  ngOnInit() {
    this.locationService
      .query()
      .subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.transactionTypeService
      .query()
      .subscribe(
        (res: HttpResponse<ITransactionType[]>) => (this.transactiontypes = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.supplierService
      .query()
      .subscribe((res: HttpResponse<ISupplier[]>) => (this.suppliers = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISupplierAccount) {
    return item.id;
  }

  registerChangeInSupplierAccounts() {
    this.eventSubscriber = this.eventManager.subscribe('supplierAccountListModification', () => this.reset());
  }

  delete(supplierAccount: ISupplierAccount) {
    const modalRef = this.modalService.open(SupplierAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.supplierAccount = supplierAccount;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  protected paginateSupplierAccounts(data: ISupplierAccount[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.supplierAccounts.push(data[i]);
    }
  }
}

import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ICustomerAccountBalance, CustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';
import { CustomerAccountBalanceService } from './customer-account-balance.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

@Component({
  selector: 'jhi-customer-account-balance-update',
  templateUrl: './customer-account-balance-update.component.html'
})
export class CustomerAccountBalanceUpdateComponent implements OnInit {
  isSaving: boolean;

  locations: ILocation[];

  customers: ICustomer[];

  transactiontypes: ITransactionType[];

  editForm = this.fb.group({
    id: [],
    balance: [null, [Validators.required]],
    location: [null, Validators.required],
    customer: [null, Validators.required],
    transactionType: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected customerAccountBalanceService: CustomerAccountBalanceService,
    protected locationService: LocationService,
    protected customerService: CustomerService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ customerAccountBalance }) => {
      this.updateForm(customerAccountBalance);
    });
    this.locationService
      .query()
      .subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.customerService
      .query()
      .subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.transactionTypeService
      .query()
      .subscribe(
        (res: HttpResponse<ITransactionType[]>) => (this.transactiontypes = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(customerAccountBalance: ICustomerAccountBalance) {
    this.editForm.patchValue({
      id: customerAccountBalance.id,
      balance: customerAccountBalance.balance,
      location: customerAccountBalance.location,
      customer: customerAccountBalance.customer,
      transactionType: customerAccountBalance.transactionType
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const customerAccountBalance = this.createFromForm();
    if (customerAccountBalance.id !== undefined) {
      this.subscribeToSaveResponse(this.customerAccountBalanceService.update(customerAccountBalance));
    } else {
      this.subscribeToSaveResponse(this.customerAccountBalanceService.create(customerAccountBalance));
    }
  }

  private createFromForm(): ICustomerAccountBalance {
    return {
      ...new CustomerAccountBalance(),
      id: this.editForm.get(['id']).value,
      balance: this.editForm.get(['balance']).value,
      location: this.editForm.get(['location']).value,
      customer: this.editForm.get(['customer']).value,
      transactionType: this.editForm.get(['transactionType']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerAccountBalance>>) {
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

  trackCustomerById(index: number, item: ICustomer) {
    return item.id;
  }

  trackTransactionTypeById(index: number, item: ITransactionType) {
    return item.id;
  }
}

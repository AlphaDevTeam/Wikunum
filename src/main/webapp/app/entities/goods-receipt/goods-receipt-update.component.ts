import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IGoodsReceipt, GoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from './goods-receipt.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier/supplier.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

import { IGoodsReceiptDetails, GoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { GoodsReceiptDetailsService } from '../goods-receipt-details/goods-receipt-details.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';
import { IStorageBin } from 'app/shared/model/storage-bin.model';
import { StorageBinService } from 'app/entities/storage-bin/storage-bin.service';

@Component({
  selector: 'jhi-goods-receipt-update',
  templateUrl: './goods-receipt-update.component.html'
})
export class GoodsReceiptUpdateComponent implements OnInit {
  isSaving: boolean;

  suppliers: ISupplier[];

  locations: ILocation[];
  grnDateDp: any;

  debug: any;
  // Details Items
  editFieldQty: number;
  editFieldCost: number;
  items: IItems[];
  storagebins: IStorageBin[];
  grnList: IGoodsReceiptDetails[];
  virtualGoodsReceipts: IGoodsReceiptDetails[];

  editForm = this.fb.group({
    id: [],
    grnNumber: [null, [Validators.required]],
    grnDate: [null, [Validators.required]],
    poNumber: [],
    supplier: [null, Validators.required],
    location: [null, Validators.required],
    grnQty: [null, [Validators.required]],
    item: [],
    storageBin: [],
    revisedItemCost: [],
    grn: [],
    itemCost: []
  });

  editFormDetails = this.fb.group({});

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected goodsReceiptService: GoodsReceiptService,
    protected supplierService: SupplierService,
    protected locationService: LocationService,
    protected storageBinService: StorageBinService,
    protected goodsReceiptDetailsService: GoodsReceiptDetailsService,
    protected itemsService: ItemsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    // Details related
    this.grnList = [];

    this.activatedRoute.data.subscribe(({ goodsReceipt }) => {
      this.updateForm(goodsReceipt);
      this.grnList = goodsReceipt.details;
      this.virtualGoodsReceipts = this.grnList;
    });
    this.supplierService
      .query()
      .subscribe((res: HttpResponse<ISupplier[]>) => (this.suppliers = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.locationService
      .query()
      .subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body), (res: HttpErrorResponse) => this.onError(res.message));

    this.itemsService
      .query()
      .subscribe((res: HttpResponse<IItems[]>) => (this.items = res.body), (res: HttpErrorResponse) => this.onError(res.message));

    this.storageBinService
      .query()
      .subscribe(
        (res: HttpResponse<IStorageBin[]>) => (this.storagebins = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateFormDetails(goodsReceiptDetails: IGoodsReceiptDetails) {
    this.editForm.patchValue({
      id: goodsReceiptDetails.id,
      grnQty: goodsReceiptDetails.grnQty,
      revisedItemCost: goodsReceiptDetails.revisedItemCost,
      item: goodsReceiptDetails.item,
      storageBin: goodsReceiptDetails.storageBin,
      grn: goodsReceiptDetails.grn
    });
  }

  updateForm(goodsReceipt: IGoodsReceipt) {
    this.editForm.patchValue({
      id: goodsReceipt.id,
      grnNumber: goodsReceipt.grnNumber,
      grnDate: goodsReceipt.grnDate,
      poNumber: goodsReceipt.poNumber,
      supplier: goodsReceipt.supplier,
      location: goodsReceipt.location
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const goodsReceipt = this.createFromForm();
    const goodsReceiptDetails = this.virtualGoodsReceipts;
    goodsReceipt.details = goodsReceiptDetails;
    if (goodsReceipt.id !== undefined) {
      this.subscribeToSaveResponse(this.goodsReceiptService.update(goodsReceipt));
    } else {
      this.subscribeToSaveResponse(this.goodsReceiptService.create(goodsReceipt));
    }
  }

  private createFromForm(): IGoodsReceipt {
    return {
      ...new GoodsReceipt(),
      id: this.editForm.get(['id']).value,
      grnNumber: this.editForm.get(['grnNumber']).value,
      grnDate: this.editForm.get(['grnDate']).value,
      poNumber: this.editForm.get(['poNumber']).value,
      supplier: this.editForm.get(['supplier']).value,
      location: this.editForm.get(['location']).value
    };
  }

  private createFromFormDetails(): IGoodsReceiptDetails {
    return {
      ...new GoodsReceiptDetails(),
      id: this.editForm.get(['id']).value,
      grnQty: this.editForm.get(['grnQty']).value,
      revisedItemCost: this.editForm.get(['revisedItemCost']).value,
      item: this.editForm.get(['item']).value,
      storageBin: this.editForm.get(['storageBin']).value,
      grn: this.editForm.get(['grn']).value
    };
  }

  addItem() {
    const goodsReceiptDetails = this.createFromFormDetails();
    if (this.grnList === undefined) {
      this.grnList = [];
    }
    this.grnList.push(goodsReceiptDetails);
    this.virtualGoodsReceipts = this.grnList;
    this.debug = 'Debug : ' + JSON.stringify(goodsReceiptDetails);
  }

  updateList(id: number, property: string, event: any) {
    const editFieldQty = event.target.textContent;
    this.virtualGoodsReceipts[id][property] = editFieldQty;
  }

  changeValue(id: number, property: string, event: any) {
    this.editFieldQty = event.target.textContent;
  }

  removeItem(id: number) {
    this.grnList.splice(id, 1);
    this.virtualGoodsReceipts = this.grnList;
  }

  updateItem(item: IItems) {
    this.editForm.patchValue({
      itemCost: item.itemCost
    });
    this.debug = 'updated' + item;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoodsReceipt>>) {
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

  trackSupplierById(index: number, item: ISupplier) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }

  trackGoodsReceiptById(index: number, item: IGoodsReceipt) {
    return item.id;
  }

  trackItemsById(index: number, item: IItems) {
    return item.id;
  }

  trackId(index: number, item: IGoodsReceiptDetails) {
    return item.id;
  }

  trackStorageBinById(index: number, item: IStorageBin) {
    return item.id;
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

import { ExUserService } from 'app/entities/ex-user/ex-user.service';
import { IExUser } from 'app/shared/model/ex-user.model';

import { IMenuItems } from 'app/shared/model/menu-items.model';
import { MenuItemsService } from 'app/entities/menu-items/menu-items.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account;
  menuItems: IMenuItems[];
  eventSubscriber: Subscription;
  exuser: IExUser;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;
  authSubscription: Subscription;
  modalRef: NgbModalRef;

  constructor(
    private accountService: AccountService,
    private menuItemsService: MenuItemsService,
    private exUserService: ExUserService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    private parseLinks: JhiParseLinks
  ) {}

  ngOnInit() {
    this.menuItems = [];

    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();

    this.loadAll();
    this.registerChangeInMenuItems();
  }

  loadAll() {
    this.menuItemsService
      .query({ 'isActive.equals': true })
      .subscribe((res: HttpResponse<IMenuItems[]>) => this.paginateMenuItems(res.body, res.headers));
  }

  registerChangeInMenuItems() {
    this.eventSubscriber = this.eventManager.subscribe('menuItemsListModification', () => this.reset());
  }

  registerAuthenticationSuccess() {
    this.authSubscription = this.eventManager.subscribe('authenticationSuccess', () => {
      this.accountService.identity().subscribe(account => {
        this.account = account;
      });
    });
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
  reset() {
    this.page = 0;
    this.menuItems = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  protected paginateMenuItems(data: IMenuItems[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.menuItems.push(data[i]);
    }
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.eventManager.destroy(this.authSubscription);
    }
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserGroups } from 'app/shared/model/user-groups.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { UserGroupsService } from './user-groups.service';
import { UserGroupsDeleteDialogComponent } from './user-groups-delete-dialog.component';

@Component({
  selector: 'jhi-user-groups',
  templateUrl: './user-groups.component.html'
})
export class UserGroupsComponent implements OnInit, OnDestroy {
  userGroups: IUserGroups[];
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;

  constructor(
    protected userGroupsService: UserGroupsService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.userGroups = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
    this.userGroupsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IUserGroups[]>) => this.paginateUserGroups(res.body, res.headers));
  }

  reset() {
    this.page = 0;
    this.userGroups = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInUserGroups();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IUserGroups) {
    return item.id;
  }

  registerChangeInUserGroups() {
    this.eventSubscriber = this.eventManager.subscribe('userGroupsListModification', () => this.reset());
  }

  delete(userGroups: IUserGroups) {
    const modalRef = this.modalService.open(UserGroupsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userGroups = userGroups;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateUserGroups(data: IUserGroups[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.userGroups.push(data[i]);
    }
  }
}

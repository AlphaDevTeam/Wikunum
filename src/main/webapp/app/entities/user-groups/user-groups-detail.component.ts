import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserGroups } from 'app/shared/model/user-groups.model';

@Component({
  selector: 'jhi-user-groups-detail',
  templateUrl: './user-groups-detail.component.html'
})
export class UserGroupsDetailComponent implements OnInit {
  userGroups: IUserGroups;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userGroups }) => {
      this.userGroups = userGroups;
    });
  }

  previousState() {
    window.history.back();
  }
}

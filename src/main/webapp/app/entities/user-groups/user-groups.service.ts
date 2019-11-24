import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUserGroups } from 'app/shared/model/user-groups.model';

type EntityResponseType = HttpResponse<IUserGroups>;
type EntityArrayResponseType = HttpResponse<IUserGroups[]>;

@Injectable({ providedIn: 'root' })
export class UserGroupsService {
  public resourceUrl = SERVER_API_URL + 'api/user-groups';

  constructor(protected http: HttpClient) {}

  create(userGroups: IUserGroups): Observable<EntityResponseType> {
    return this.http.post<IUserGroups>(this.resourceUrl, userGroups, { observe: 'response' });
  }

  update(userGroups: IUserGroups): Observable<EntityResponseType> {
    return this.http.put<IUserGroups>(this.resourceUrl, userGroups, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserGroups>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserGroups[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

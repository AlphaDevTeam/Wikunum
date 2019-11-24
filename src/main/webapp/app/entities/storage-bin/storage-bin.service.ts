import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStorageBin } from 'app/shared/model/storage-bin.model';

type EntityResponseType = HttpResponse<IStorageBin>;
type EntityArrayResponseType = HttpResponse<IStorageBin[]>;

@Injectable({ providedIn: 'root' })
export class StorageBinService {
  public resourceUrl = SERVER_API_URL + 'api/storage-bins';

  constructor(protected http: HttpClient) {}

  create(storageBin: IStorageBin): Observable<EntityResponseType> {
    return this.http.post<IStorageBin>(this.resourceUrl, storageBin, { observe: 'response' });
  }

  update(storageBin: IStorageBin): Observable<EntityResponseType> {
    return this.http.put<IStorageBin>(this.resourceUrl, storageBin, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStorageBin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStorageBin[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

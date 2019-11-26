import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUnitOfMeasurements } from 'app/shared/model/unit-of-measurements.model';

type EntityResponseType = HttpResponse<IUnitOfMeasurements>;
type EntityArrayResponseType = HttpResponse<IUnitOfMeasurements[]>;

@Injectable({ providedIn: 'root' })
export class UnitOfMeasurementsService {
  public resourceUrl = SERVER_API_URL + 'api/unit-of-measurements';

  constructor(protected http: HttpClient) {}

  create(unitOfMeasurements: IUnitOfMeasurements): Observable<EntityResponseType> {
    return this.http.post<IUnitOfMeasurements>(this.resourceUrl, unitOfMeasurements, { observe: 'response' });
  }

  update(unitOfMeasurements: IUnitOfMeasurements): Observable<EntityResponseType> {
    return this.http.put<IUnitOfMeasurements>(this.resourceUrl, unitOfMeasurements, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUnitOfMeasurements>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUnitOfMeasurements[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

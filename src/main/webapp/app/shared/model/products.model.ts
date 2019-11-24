import { ILocation } from 'app/shared/model/location.model';

export interface IProducts {
  id?: number;
  productCode?: string;
  productName?: string;
  productPrefix?: string;
  location?: ILocation;
}

export class Products implements IProducts {
  constructor(
    public id?: number,
    public productCode?: string,
    public productName?: string,
    public productPrefix?: string,
    public location?: ILocation
  ) {}
}

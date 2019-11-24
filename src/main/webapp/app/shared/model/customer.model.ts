import { ILocation } from 'app/shared/model/location.model';

export interface ICustomer {
  id?: number;
  customerCode?: string;
  customerName?: string;
  customerCreditLimit?: number;
  isActive?: boolean;
  location?: ILocation;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public customerCode?: string,
    public customerName?: string,
    public customerCreditLimit?: number,
    public isActive?: boolean,
    public location?: ILocation
  ) {
    this.isActive = this.isActive || false;
  }
}

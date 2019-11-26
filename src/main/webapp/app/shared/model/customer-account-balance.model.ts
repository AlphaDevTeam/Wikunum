import { ILocation } from 'app/shared/model/location.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';

export interface ICustomerAccountBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
  customer?: ICustomer;
  transactionType?: ITransactionType;
}

export class CustomerAccountBalance implements ICustomerAccountBalance {
  constructor(
    public id?: number,
    public balance?: number,
    public location?: ILocation,
    public customer?: ICustomer,
    public transactionType?: ITransactionType
  ) {}
}

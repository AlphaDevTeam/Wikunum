export interface ITransactionType {
  id?: number;
  transactionypeCode?: string;
  transactionType?: string;
  isActive?: boolean;
}

export class TransactionType implements ITransactionType {
  constructor(public id?: number, public transactionypeCode?: string, public transactionType?: string, public isActive?: boolean) {
    this.isActive = this.isActive || false;
  }
}

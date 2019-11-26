export interface IUnitOfMeasurements {
  id?: number;
  uomCode?: string;
  uomDescription?: string;
}

export class UnitOfMeasurements implements IUnitOfMeasurements {
  constructor(public id?: number, public uomCode?: string, public uomDescription?: string) {}
}

export interface IUOM {
  id?: number;
  uomCode?: string;
  uomDescription?: string;
}

export class UOM implements IUOM {
  constructor(public id?: number, public uomCode?: string, public uomDescription?: string) {}
}

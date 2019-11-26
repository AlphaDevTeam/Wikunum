import { IDocumentType } from 'app/shared/model/document-type.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IDocumentNumberConfig {
  id?: number;
  documentPrefix?: string;
  documentPostfix?: string;
  currentNumber?: number;
  document?: IDocumentType;
  location?: ILocation;
}

export class DocumentNumberConfig implements IDocumentNumberConfig {
  constructor(
    public id?: number,
    public documentPrefix?: string,
    public documentPostfix?: string,
    public currentNumber?: number,
    public document?: IDocumentType,
    public location?: ILocation
  ) {}
}

import {OperationModel} from "./operation.model";

export class HistoryModel{
  accountId!: string;
  currency!: string;
  balance!: number;
  currentPage!: number;
  totalPages!: number;
  pageSize!: number;
  operations!: OperationModel[];
}

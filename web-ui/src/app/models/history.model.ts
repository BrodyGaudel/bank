import {OperationModel} from "./operation.model";

export class HistoryModel{
  customerId!: string;
  accountId!: string;
  currency!: string;
  balance!: number;
  currentPage!: number;
  totalPages!: number;
  pageSize!: number;
  operations!: OperationModel[];
}

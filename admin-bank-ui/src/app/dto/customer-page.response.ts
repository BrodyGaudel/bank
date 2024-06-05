import {CustomerResponse} from "./customer.response";
import {PageStatus} from "./page-status";
import {PaginationInfo} from "./pagination-info";

export class CustomerPageResponse {
  pagination!: PaginationInfo;
  status!: PageStatus;
  customers!: CustomerResponse[];
}

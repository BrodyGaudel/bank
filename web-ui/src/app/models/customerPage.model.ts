import {CustomerModel} from "./customer.model";

export class CustomerPageModel{
    page!: number;
    size!: number;
    totalPage!: number;
    customerDTOList!: CustomerModel[];
}

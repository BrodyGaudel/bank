import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CustomerModel} from "../../models/customer.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private host: string = 'http://localhost:8888/CUSTOMER-SERVICE/bank/api/customers/';

  constructor(private http: HttpClient) { }

  createCustomer(model: CustomerModel) : Observable<CustomerModel> {
    return this.http.post<CustomerModel>(this.host +'save', model);
  }

  updateCustomer(id: string, model: CustomerModel) : Observable<CustomerModel> {
    return this.http.put<CustomerModel>(this.host +'update/'+id, model);
  }

  getCustomerById(id: string) : Observable<CustomerModel> {
    return this.http.get<CustomerModel>(this.host +'get/'+id);
  }

  getCustomerByCin(cin: string) : Observable<CustomerModel> {
    return this.http.get<CustomerModel>(this.host +'get/'+cin);
  }

  getAllCustomers(size: number, page: number) : Observable<Array<CustomerModel>> {
    return this.http.get<Array<CustomerModel>>(this.host +'list/'+size+'/'+page);
  }

  searchCustomer(keyword: string, size: number, page: number) : Observable<Array<CustomerModel>> {
    return this.http.get<Array<CustomerModel>>(this.host +size +'/' +page +'/search?keyword=' +keyword);
  }

}

import { Injectable } from '@angular/core';
import {CustomerModel} from "../../models/customer.model";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private host: string = 'http://localhost:8888/CUSTOMER-SERVICE/bank/customers/';

  constructor(private http: HttpClient) { }

  public createCustomer(model: CustomerModel) : Observable<CustomerModel> {
    return this.http.post<CustomerModel>(this.host +'create', model);
  }

  public updateCustomer(id: string, model: CustomerModel) : Observable<CustomerModel> {
    return this.http.put<CustomerModel>(this.host +'update/'+id, model);
  }

  public getCustomerById(id: string) : Observable<CustomerModel> {
    return this.http.get<CustomerModel>(this.host +'get/'+id);
  }

  public getCustomerByCin(cin: string) : Observable<CustomerModel> {
    return this.http.get<CustomerModel>(this.host +'get/'+cin);
  }

  public getAllCustomers(size: number, page: number) : Observable<Array<CustomerModel>> {
    return this.http.get<Array<CustomerModel>>(this.host +'list/'+size+'/'+page);
  }

  public searchCustomer(keyword: string, size: number, page: number) : Observable<Array<CustomerModel>> {
    return this.http.get<Array<CustomerModel>>(this.host +size +'/' +page +'/search?keyword=' +keyword);
  }

  public deleteCustomerById(id: string){
    return this.http.delete(this.host +'delete/' +id);
  }
}

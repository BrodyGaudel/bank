import { Injectable } from '@angular/core';
import {CustomerModel} from "../../models/customer.model";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthService} from "../auth-service/auth.service";

const httpOptions = {
  headers: new HttpHeaders( {'Content-Type': 'application/json'} )
};

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private host: string = 'http://localhost:8888/CUSTOMER-SERVICE/bank/customers/';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getHttpHeaders() : HttpHeaders{
    let jwt : string = this.authService.getToken();
    jwt = "Bearer "+jwt;
    console.log(jwt);
    return new HttpHeaders({"Authorization": jwt});
  }

  public createCustomer(model: CustomerModel) : Observable<CustomerModel> {
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.post<CustomerModel>(this.host +'create', model, {headers:httpHeaders});
  }

  public updateCustomer(id: string, model: CustomerModel) : Observable<CustomerModel> {
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.put<CustomerModel>(this.host +'update/'+id, model, {headers:httpHeaders});
  }

  public getCustomerById(id: string) : Observable<CustomerModel> {
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.get<CustomerModel>(this.host +'get/'+id, {headers:httpHeaders});
  }

  public getCustomerByCin(cin: string) : Observable<CustomerModel> {
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.get<CustomerModel>(this.host +'get/'+cin, {headers:httpHeaders});
  }

  public getAllCustomers(size: number, page: number) : Observable<Array<CustomerModel>> {
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.get<Array<CustomerModel>>(this.host +'list/'+size+'/'+page, {headers:httpHeaders});
  }

  public searchCustomer(keyword: string, size: number, page: number) : Observable<Array<CustomerModel>> {
    return this.http.get<Array<CustomerModel>>(this.host +size +'/' +page +'/search?keyword=' +keyword);
  }

  public deleteCustomerById(id: string){
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.delete(this.host +'delete/' +id, {headers:httpHeaders});
  }
}

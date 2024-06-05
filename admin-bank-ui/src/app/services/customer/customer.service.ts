import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CustomerResponse} from "../../dto/customer.response";
import {CustomerRequest} from "../../dto/customer.request";
import {CustomerPageResponse} from "../../dto/customer-page.response";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private host: string = 'http://localhost:8888/CUSTOMER-SERVICE/bank/customers';

  constructor(private http: HttpClient) { }

  getCustomerById(id: string): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(this.host + '/get/' + id);
  }

  getCustomerByCin(cin: string): Observable<CustomerResponse> {
    let url: string = this.host + + '/find/' + cin;
    return this.http.get<CustomerResponse>(url);
  }

  getAllCustomers(page: number = 0, size: number = 10): Observable<CustomerPageResponse> {
    let url: string = this.host + '/list/?page=' + page +'&size=' + size;
    return this.http.get<CustomerPageResponse>(url);
  }

  searchCustomers(keyword: string = ' ', page: number = 0, size: number = 10): Observable<CustomerPageResponse> {
    return this.http.get<CustomerPageResponse>(this.host + '/search?keyword=' + keyword + '&page=' + page + '&size=' + size);
  }

  createCustomer(request: CustomerRequest): Observable<CustomerResponse> {
    return this.http.post<CustomerResponse>(this.host + '/create', request);
  }

  updateCustomer(id: string, request: CustomerRequest): Observable<CustomerResponse> {
    let url: string = this.host + '/update/' + id;
    return this.http.put<CustomerResponse>(url, request);
  }

  deleteCustomer(id: string): Observable<void> {
    let url: string = this.host + '/delete/' + id;
    return this.http.delete<void>(url);
  }


}

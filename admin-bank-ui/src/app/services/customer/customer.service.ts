import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {CustomerResponse} from "../../dto/customer/customer.response";
import {CustomerRequest} from "../../dto/customer/customer.request";
import {CustomerPageResponse} from "../../dto/customer/customer-page.response";
import {AuthService} from "../security/auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private host: string = 'http://localhost:8888/CUSTOMER-SERVICE/bank/customers';

  constructor(private http: HttpClient, private authService: AuthService) { }

  public getCustomerById(id: string): Observable<CustomerResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/get/' + id;
    return this.http.get<CustomerResponse>(url, {headers: httpHeaders});
  }

  public getCustomerByCin(cin: string): Observable<CustomerResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    let url: string = this.host + + '/find/' + cin;
    return this.http.get<CustomerResponse>(url, {headers: httpHeaders});
  }

  public getAllCustomers(page: number = 0, size: number = 10): Observable<CustomerPageResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    let url: string = this.host + '/list/?page=' + page +'&size=' + size;
    return this.http.get<CustomerPageResponse>(url, {headers: httpHeaders});
  }

  public searchCustomers(keyword: string = ' ', page: number = 0, size: number = 10): Observable<CustomerPageResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    let url: string = this.host + '/search?keyword=' + keyword + '&page=' + page + '&size=' + size;
    return this.http.get<CustomerPageResponse>(url, {headers: httpHeaders});
  }

  public createCustomer(request: CustomerRequest): Observable<CustomerResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    let url: string = this.host + '/create';
    return this.http.post<CustomerResponse>(url, request, {headers: httpHeaders});
  }

  public updateCustomer(id: string, request: CustomerRequest): Observable<CustomerResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    let url: string = this.host + '/update/' + id;
    return this.http.put<CustomerResponse>(url, request, {headers: httpHeaders});
  }

  public deleteCustomer(id: string): Observable<void> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    let url: string = this.host + '/delete/' + id;
    return this.http.delete<void>(url, {headers: httpHeaders});
  }

}

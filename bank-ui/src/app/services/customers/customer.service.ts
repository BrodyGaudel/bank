import { Injectable } from '@angular/core';
import {AuthService} from "../security/auth/auth.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {CustomerResponse} from "../../models/response/customer.response";
import {CustomerRequest} from "../../models/request/customer.request";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  host: string = "http://localhost:8888/CUSTOMER-SERVICE/bank/customers";

  constructor(private http: HttpClient, private authService: AuthService) { }

  public getCustomerById(id: string) : Observable<CustomerResponse> {
    const httpHeaders : HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.host + "/get/" + id;
    return this.http.get<CustomerResponse>(url, { headers: httpHeaders });
  }

  public searchCustomer(keyword: string, page: number, size: number): Observable<Array<CustomerResponse>> {
    const httpHeaders : HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.host + "/search?keyword=" + keyword + "&page=" + page + "&size=" + size;
    return this.http.get<Array<CustomerResponse>>(url, { headers: httpHeaders });
  }

  public createCustomer(request: CustomerRequest): Observable<CustomerResponse>{
    const httpHeaders : HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.host + "/create";
    return this.http.post<CustomerResponse>(url, request, { headers: httpHeaders });
  }

  public updateCustomer(id: string, request: CustomerRequest): Observable<CustomerResponse>{
    const httpHeaders : HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.host + "/update/" + id;
    return this.http.put<CustomerResponse>(url, request, { headers: httpHeaders });
  }

  public deleteCustomer(id: string): Observable<void> {
    const httpHeaders : HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.host + "/delete/" + id;
    return this.http.delete<void>(url, { headers: httpHeaders });
  }
}

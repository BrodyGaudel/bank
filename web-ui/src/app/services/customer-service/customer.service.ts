import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {CustomerModel} from "../../models/customer.model";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private host: string = 'http://localhost:8888/CUSTOMER-SERVICE/bank/v2/customers';

  constructor(private http: HttpClient) { }

  getById(id: string) :Observable<CustomerModel>{
    return this.http.get<CustomerModel>(this.host + '/get/' + id);
  }
}

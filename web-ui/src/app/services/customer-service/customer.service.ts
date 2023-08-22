import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private url: string = 'http://localhost:8888/CUSTOMER-SERVICE/bank/api';

  constructor() { }
}

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private url: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/api';

  constructor() { }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../../models/account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private host: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/accounts/';

  constructor(private http: HttpClient) { }

  public getAccountByCustomerId(customerId: string): Observable<AccountModel>{
    return this.http.get<AccountModel>(this.host+ 'find/'+ customerId);
  }

  public getAccountById(id: string): Observable<AccountModel>{
    return this.http.get<AccountModel>(this.host+ 'get/'+ id);
  }

  public saveAccount(model: AccountModel): Observable<AccountModel>{
    return this.http.post<AccountModel>(this.host+ 'create', model);
  }

  public updateAccountStatus(model: AccountModel): Observable<AccountModel>{
    return this.http.put<AccountModel>(this.host+ 'update-status', model);
  }

}

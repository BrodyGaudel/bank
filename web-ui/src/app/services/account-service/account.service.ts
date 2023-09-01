import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../../models/account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private host: string = 'http://localhost:8886/bank/api/accounts/';

  constructor(private http: HttpClient) { }

  public createAccount(model: AccountModel) : Observable<AccountModel> {
    return this.http.post<AccountModel>(this.host +'create', model);
  }

  public updateAccountStatus(id: string, model: AccountModel) : Observable<AccountModel> {
    return this.http.put<AccountModel>(this.host +'update-status/'+id, model);
  }

  public getAccountById(id: string) : Observable<AccountModel> {
    return this.http.get<AccountModel>(this.host +'get/'+id);
  }

  public getAccountByCustomerId(customerId: string) : Observable<AccountModel> {
    return this.http.get<AccountModel>(this.host +'find/' +customerId);
  }

  public deleteAccountById(id: string) : Observable<Object>{
    return this.http.delete(this.host +'delete/' +id);
  }
}

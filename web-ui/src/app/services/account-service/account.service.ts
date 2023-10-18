import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../../models/account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private host: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/v2/accounts';

  constructor(private http: HttpClient) { }

  public getById(id: string) :Observable<AccountModel>{
    return this.http.get<AccountModel>(this.host + '/get/' + id);
  }

  public getByCustomerId(customerId: string) :Observable<AccountModel>{
    return this.http.get<AccountModel>(this.host + '/find/' + customerId);
  }

  public save(model: AccountModel) :Observable<AccountModel>{
    return this.http.post<AccountModel>(this.host + '/save', model);
  }

  public updateStatus(model: AccountModel) :Observable<AccountModel>{
    return this.http.put<AccountModel>(this.host + '/update', model);
  }

  public deleteById(id: string) :Observable<Object>{
    return this.http.delete(this.host + '/delete/' + id);
  }
}

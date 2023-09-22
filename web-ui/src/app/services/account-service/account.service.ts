import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../../models/account.model";
import {AuthService} from "../auth-service/auth.service";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private host: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/accounts/';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getHttpHeaders() : HttpHeaders{
    let jwt : string = this.authService.getToken();
    jwt = "Bearer "+jwt;
    console.log(jwt);
    return new HttpHeaders({"Authorization": jwt});
  }

  public getAccountByCustomerId(customerId: string): Observable<AccountModel>{
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.get<AccountModel>(this.host+ 'find/'+ customerId, {headers:httpHeaders});
  }

  public getAccountById(id: string): Observable<AccountModel>{
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.get<AccountModel>(this.host+ 'get/'+ id, {headers:httpHeaders});
  }

  public saveAccount(model: AccountModel): Observable<AccountModel>{
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.post<AccountModel>(this.host+ 'create', model, {headers:httpHeaders});
  }

  public updateAccountStatus(model: AccountModel): Observable<AccountModel>{
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.put<AccountModel>(this.host+ 'update-status', model, {headers:httpHeaders});
  }

}

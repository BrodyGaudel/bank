import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../../models/account.model";
import {AuthService} from "../auth-service/auth.service";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private host: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/v2/accounts';

  constructor(private http: HttpClient, private authService: AuthService){}

  private getHeader() :HttpHeaders {
    let jwt: string = this.authService.getToken();
    jwt = "Bearer "+jwt;
    return new HttpHeaders({"Authorization": jwt});
  }

  public getById(id: string) :Observable<AccountModel>{
    let httpHeaders: HttpHeaders = this.getHeader();
    return this.http.get<AccountModel>(this.host + '/get/' + id, {headers:httpHeaders});
  }

  public getByCustomerId(customerId: string) :Observable<AccountModel>{
    let httpHeaders: HttpHeaders = this.getHeader();
    return this.http.get<AccountModel>(this.host + '/find/' + customerId, {headers:httpHeaders});
  }

  public save(model: AccountModel) :Observable<AccountModel>{
    let httpHeaders: HttpHeaders = this.getHeader();
    return this.http.post<AccountModel>(this.host + '/save', model, {headers:httpHeaders});
  }

  public updateStatus(model: AccountModel) :Observable<AccountModel>{
    let httpHeaders: HttpHeaders = this.getHeader();
    return this.http.put<AccountModel>(this.host + '/update', model, {headers:httpHeaders});
  }

  public deleteById(id: string) :Observable<Object>{
    let httpHeaders: HttpHeaders = this.getHeader();
    return this.http.delete(this.host + '/delete/' + id, {headers:httpHeaders});
  }
}

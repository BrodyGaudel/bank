import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CreditModel} from "../../models/credit.model";
import {DebitModel} from "../../models/debit.model";
import {Observable} from "rxjs";
import {OperationModel} from "../../models/operation.model";
import {HistoryModel} from "../../models/history.model";
import {AuthService} from "../auth-service/auth.service";

@Injectable({
  providedIn: 'root'
})
export class OperationService {

  private host: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/operations/';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getHttpHeaders() : HttpHeaders{
    let jwt : string = this.authService.getToken();
    jwt = "Bearer "+jwt;
    console.log(jwt);
    return new HttpHeaders({"Authorization": jwt});
  }

  public creditAccount(model: CreditModel) : Observable<CreditModel>{
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.post<CreditModel>(this.host + 'credit', model, {headers:httpHeaders});
  }

  public debitAccount(model: DebitModel) : Observable<DebitModel>{
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.post<DebitModel>(this.host + 'debit', model, {headers:httpHeaders});
  }

  public getOperationById(id: string) : Observable<OperationModel>{
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.get<OperationModel>(this.host + 'get/' +id, {headers:httpHeaders});
  }

  public getHistory(accountId: string, size: number, page: number) : Observable<HistoryModel>{
    let httpHeaders : HttpHeaders = this.getHttpHeaders();
    return this.http.get<HistoryModel>(this.host + accountId + '/history?page='+page+'&size=' + size, {headers:httpHeaders});
  }
}

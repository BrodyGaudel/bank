import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CreditModel} from "../../models/credit.model";
import {Observable} from "rxjs";
import {DebitModel} from "../../models/debit.model";
import {OperationModel} from "../../models/operation.model";
import {HistoryModel} from "../../models/history.model";
import {AuthService} from "../auth-service/auth.service";

@Injectable({
  providedIn: 'root'
})
export class OperationService {

  private host: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/v2/operations';

  constructor(private http: HttpClient, private authService: AuthService){}

  private getHeader() :HttpHeaders {
    let jwt: string = this.authService.getToken();
    jwt = "Bearer "+jwt;
    return new HttpHeaders({"Authorization": jwt});
  }

  public credit(model: CreditModel) :Observable<CreditModel>{
    let httpHeaders: HttpHeaders = this.getHeader();
    return this.http.post<CreditModel>(this.host + '/credit', model, {headers:httpHeaders});
  }

  public debit(model: DebitModel) :Observable<DebitModel>{
    let httpHeaders: HttpHeaders = this.getHeader();
    return this.http.post<DebitModel>(this.host + '/debit', model, {headers:httpHeaders});
  }

  public getById(id: string) :Observable<OperationModel>{
    let httpHeaders: HttpHeaders = this.getHeader();
    return this.http.get<OperationModel>(this.host + '/get/' + id, {headers:httpHeaders});
  }

  public getHistory(accountId: string, page: number, size: number): Observable<HistoryModel>{
    let httpHeaders: HttpHeaders = this.getHeader();
    return this.http.get<HistoryModel>(this.host + '/' + accountId + '/all?page=' + page + '&size=' +size, {headers:httpHeaders});
  }
}

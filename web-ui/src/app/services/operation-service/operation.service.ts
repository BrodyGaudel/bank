import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CreditModel} from "../../models/credit.model";
import {Observable} from "rxjs";
import {DebitModel} from "../../models/debit.model";
import {OperationModel} from "../../models/operation.model";
import {HistoryModel} from "../../models/history.model";

@Injectable({
  providedIn: 'root'
})
export class OperationService {

  private host: string = 'http://localhost:8886/bank/api/operations/';

  constructor(private http: HttpClient) { }

  public creditAccount(model: CreditModel) : Observable<CreditModel>{
    return this.http.post<CreditModel>(this.host + 'credit', model);
  }

  public debitAccount(model: DebitModel) : Observable<DebitModel>{
    return this.http.post<DebitModel>(this.host + 'debit', model);
  }

  public getOperationById(id: string) : Observable<OperationModel>{
    return this.http.get<OperationModel>(this.host + 'get/' +id);
  }

  public getHistory(accountId: string, size: number, page: number) : Observable<HistoryModel>{
    return this.http.get<HistoryModel>(this.host + accountId + '/history?page='+page+'&size=' + size);
  }

}

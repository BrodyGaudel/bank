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

  private host: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/v2/operations';

  constructor(private http: HttpClient) { }

  public credit(model: CreditModel) :Observable<CreditModel>{
    return this.http.post<CreditModel>(this.host + '/credit', model);
  }

  public debit(model: DebitModel) :Observable<DebitModel>{
    return this.http.post<DebitModel>(this.host + '/debit', model);
  }

  public getById(id: string) :Observable<OperationModel>{
    return this.http.get<OperationModel>(this.host + '/get/' + id);
  }

  public getHistory(accountId: string, page: number, size: number): Observable<HistoryModel>{
    return this.http.get<HistoryModel>(this.host + '/' + accountId + '/all?page=' + page + '&size=' +size);
  }
}

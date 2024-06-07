import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CreateAccountRequest} from "../../dto/account/create-account.request";
import {Observable} from "rxjs";
import {CreditAccountRequest} from "../../dto/account/credit-account.request";
import {DebitAccountRequest} from "../../dto/account/debit-account.request";
import {TransferRequest} from "../../dto/account/transfer.request";
import {UpdateAccountStatusRequest} from "../../dto/account/update-account-status.request";
import {AccountResponse} from "../../dto/account/account.response";
import {OperationResponse} from "../../dto/account/operation.response";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private commandHost: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/accounts/commands';
  private queryHost: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/accounts/queries';

  constructor(private http: HttpClient) { }

  public create(request: CreateAccountRequest): Observable<string> {
    return this.http.post<string>(this.commandHost + '/create', request, { responseType: 'text' as 'json' });
  }

  public delete(id: string): Observable<string> {
    return this.http.delete<string>(this.commandHost + '/delete/'+id);
  }

  public credit(request: CreditAccountRequest): Observable<string>{
    return this.http.put<string>(this.commandHost + '/credit', request, { responseType: 'text' as 'json' });
  }

  public debit(request: DebitAccountRequest): Observable<string>{
    return this.http.put<string>(this.commandHost + '/debit', request, { responseType: 'text' as 'json' });
  }

  public transfer(request: TransferRequest): Observable<string> {
    return this.http.put<string>(this.commandHost + '/transfer', request);
  }

  public update(request: UpdateAccountStatusRequest): Observable<string> {
    return this.http.put<string>(this.commandHost + '/update', request);
  }

  public findById(id: string): Observable<AccountResponse>{
    return this.http.get<AccountResponse>(this.queryHost + '/get-account/' + id);
  }

  public findByCustomerId(customerId: string): Observable<AccountResponse>{
    return this.http.get<AccountResponse>(this.queryHost + '/getaccountbycustomer/' + customerId);
  }

  public findOperationById(id: string): Observable<OperationResponse>{
    return this.http.get<OperationResponse>(this.queryHost + '/get-operation/' + id);
  }

  public findAllOperationsByAccountId(accountId: string, page: number, size: number): Observable<Array<OperationResponse>>{
    return this.http.get<Array<OperationResponse>>(this.queryHost + '/get-all-operations?accountId=' + accountId + '&page=' + page + "&size=" + size);
  }

}

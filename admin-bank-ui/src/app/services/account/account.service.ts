import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CreateAccountRequest} from "../../dto/account/create-account.request";
import {Observable} from "rxjs";
import {CreditAccountRequest} from "../../dto/account/credit-account.request";
import {DebitAccountRequest} from "../../dto/account/debit-account.request";
import {TransferRequest} from "../../dto/account/transfer.request";
import {UpdateAccountStatusRequest} from "../../dto/account/update-account-status.request";
import {AccountResponse} from "../../dto/account/account.response";
import {OperationResponse} from "../../dto/account/operation.response";
import {AuthService} from "../security/auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private commandHost: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/accounts/commands';
  private queryHost: string = 'http://localhost:8888/ACCOUNT-SERVICE/bank/accounts/queries';

  constructor(private http: HttpClient, private authService: AuthService) { }

  public create(request: CreateAccountRequest): Observable<string> {
    const options: {headers: HttpHeaders; responseType: 'json'} = this.buildHttpOptions();
    const url: string = this.commandHost + '/create'
    return this.http.post<string>(url, request, options) ;
  }

  public delete(id: string): Observable<string> {
    const options: {headers: HttpHeaders; responseType: 'json'} = this.buildHttpOptions();
    const url: string = this.commandHost + '/delete/' + id;
    return this.http.delete<string>(url, options);
  }

  public credit(request: CreditAccountRequest): Observable<string>{
    const options: {headers: HttpHeaders; responseType: 'json'} = this.buildHttpOptions();
    const url: string = this.commandHost + '/credit'
    return this.http.put<string>(url, request, options);
  }

  public debit(request: DebitAccountRequest): Observable<string>{
    const options: {headers: HttpHeaders; responseType: 'json'} = this.buildHttpOptions();
    const url: string = this.commandHost + '/debit'
    return this.http.put<string>(url, request, options);
  }

  public transfer(request: TransferRequest): Observable<string> {
    const options: {headers: HttpHeaders; responseType: 'json'} = this.buildHttpOptions();
    const url: string = this.commandHost + '/transfer'
    return this.http.put<string>(url, request, options);
  }

  public update(request: UpdateAccountStatusRequest): Observable<string> {
    const options: {headers: HttpHeaders; responseType: 'json'} = this.buildHttpOptions();
    const url: string = this.commandHost + '/update'
    return this.http.put<string>(url, request, options);
  }

  public findById(id: string): Observable<AccountResponse>{
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.queryHost + '/get-account/' +id;
    return this.http.get<AccountResponse>(url, {headers: httpHeaders});
  }

  public findByCustomerId(customerId: string): Observable<AccountResponse>{
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.queryHost + '/getaccountbycustomer/' + customerId;
    return this.http.get<AccountResponse>(url, {headers: httpHeaders});
  }

  public findOperationById(id: string): Observable<OperationResponse>{
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.queryHost + '/get-operation/' + id;
    return this.http.get<OperationResponse>(url, {headers: httpHeaders});
  }

  public findAllOperationsByAccountId(accountId: string, page: number, size: number): Observable<Array<OperationResponse>>{
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.queryHost + '/get-all-operations?accountId=' + accountId + '&page=' + page + "&size=" + size;
    return this.http.get<Array<OperationResponse>>(url, {headers: httpHeaders});
  }

  private buildHttpOptions(): {headers: HttpHeaders; responseType: 'json'} {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    return {headers: httpHeaders, responseType: 'text' as 'json'};
  }

}

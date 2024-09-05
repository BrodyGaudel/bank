import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthService} from "../security/auth/auth.service";
import {AccountRequest} from "../../models/request/account.request";
import {Observable} from "rxjs";
import {OperationRequest} from "../../models/request/operation.request";
import {UpdateStatusRequest} from "../../models/request/update-status.request";
import {AccountResponse} from "../../models/response/account.response";
import {OperationResponse} from "../../models/response/operation.response";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  commandHost: string = "http://localhost:8888/ACCOUNT-SERVICE/bank/accounts/commands";
  queryHost: string = "http://localhost:8888/ACCOUNT-SERVICE/bank/accounts/queries";

  constructor(private http: HttpClient, private authService: AuthService) { }

  public createAccount(request: AccountRequest): Observable<string> {
    const httpHeaders: HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.commandHost + "/create";
    return this.http.post<string>(url, request, { headers: httpHeaders,responseType: 'text' as 'json' });
  }

  public creditAccount(request: OperationRequest): Observable<string>{
    const httpHeaders: HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.commandHost + "/credit";
    return this.http.post<string>(url, request, { headers: httpHeaders,responseType: 'text' as 'json' });
  }

  public debitAccount(request: OperationRequest): Observable<string>{
    const httpHeaders: HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.commandHost + "/debit";
    return this.http.post<string>(url, request, { headers: httpHeaders,responseType: 'text' as 'json' });
  }

  public updateAccountStatus(request: UpdateStatusRequest): Observable<string>{
    const httpHeaders: HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.commandHost + "/update";
    return this.http.put<string>(url, request, { headers: httpHeaders,responseType: 'text' as 'json' });
  }

  public getAccountById(id: string): Observable<AccountResponse> {
    const options: {headers: HttpHeaders} = this.getQueryOptions();
    const url: string = this.queryHost + "/get-account/" + id;
    return this.http.get<AccountResponse>(url, options);
  }

  public getAccountByCustomerId(customerId: string): Observable<AccountResponse> {
    const options: {headers: HttpHeaders} = this.getQueryOptions();
    const url: string = this.queryHost + "/find-account/" + customerId;
    return this.http.get<AccountResponse>(url, options);
  }

  public getOperationById(id: string): Observable<OperationResponse> {
    const options: {headers: HttpHeaders} = this.getQueryOptions();
    const url: string = this.queryHost + "/get-operation/" + id;
    return this.http.get<OperationResponse>(url, options);
  }

  public getOperationByAccountId(accountId: string, page: number, size: number): Observable<Array<OperationResponse>> {
    const url: string = this.queryHost + "/all-operations?accountId=" + accountId + "&page=" + page + "&size=" + size;
    const options: {headers: HttpHeaders} = this.getQueryOptions();
    return this.http.get<Array<OperationResponse>>(url, options);
  }

  private getQueryOptions(): {headers: HttpHeaders} {
    const httpHeaders: HttpHeaders = this.authService.getHttpHeaders();
    return {headers: httpHeaders};
  }

  private getCommandOptions():{headers: HttpHeaders, responseType: "json"} {
    const httpHeaders: HttpHeaders = this.authService.getHttpHeaders();
    return {headers: httpHeaders, responseType: 'text' as 'json'};
  }

}

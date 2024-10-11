import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserResetPasswordRequest} from "../../models/user-reset-password.request";

@Injectable({
  providedIn: 'root'
})
export class PasswordService {

  private readonly host: string = "http://localhost:8888/AUTHENTICATION-SERVICE/bank/passwords"

  constructor(private readonly http: HttpClient) { }

  ask(email: string) : Observable<void> {
    const url: string = this.host + "/ask/" + email;
    return this.http.get<void>(url);
  }

  submit(request: UserResetPasswordRequest): Observable<void> {
    const url: string = this.host + "/reset";
    return this.http.post<void>(url, request)
  }

}

import { Injectable } from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserResponse} from "../../models/user.response";
import {UserRequest} from "../../models/user.request";
import {PageResponse} from "../../../lang/page.response";
import {UserRoleRequest} from "../../models/user-role.request";
import {UserUpdatePasswordRequest} from "../../models/user-update-password.request";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly host: string = "http://localhost:8888/AUTHENTICATION-SERVICE/bank/users"

  constructor(private readonly http: HttpClient, private readonly authService: AuthService) { }

  public search(keyword: string, page: number, size: number): Observable<PageResponse<UserResponse>> {
    const url: string = this.host + "/search?keyword=" +keyword + "&page=" + page + "&size=" + size;
    const headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.get<PageResponse<UserResponse>>(url,{headers: headers})
  }

  public getById(id: string): Observable<UserResponse>{
    const url: string = this.host + "/get/"+id;
    const headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.get<UserResponse>(url,{headers: headers})
  }

  public updateStatus(id: string): Observable<UserResponse>{
    const url: string = this.host + '/status/' + id;
    const headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.get<UserResponse>(url,{headers: headers})
  }

  public delete(id: string) :Observable<void>{
    const url: string = this.host + '/delete/'+id;
    const headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.delete<void>(url,{headers: headers})
  }

  create(request: UserRequest): Observable<UserResponse> {
    const url:string = this.host + '/create';
    const headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.post<UserResponse>(url, request, {headers: headers})
  }

  update(id: string, request: UserRequest): Observable<UserResponse> {
    const url:string = this.host + '/update/' +id;
    const headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.put<UserResponse>(url, request, {headers: headers})
  }

  addRoleToUser(request: UserRoleRequest): Observable<UserResponse> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.post<UserResponse>(`${this.host}/roles/add`, request, {headers: headers})
  }

  removeRoleFromUser(request: UserRoleRequest): Observable<UserResponse> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.post<UserResponse>(`${this.host}/roles/remove`, request, {headers: headers})
  }

  updatePassword(request: UserUpdatePasswordRequest): Observable<UserResponse> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    const url: string = this.host + '/pwd';
    return this.http.post<UserResponse>(url, request, {headers: headers})
  }
}

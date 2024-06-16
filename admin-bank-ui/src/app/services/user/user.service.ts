import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthService} from "../security/auth/auth.service";
import {UserRequest} from "../../dto/security/user.request";
import {UserResponse} from "../../dto/security/user.response";
import {Observable} from "rxjs";
import {UserRoleRequest} from "../../dto/security/user-role.request";
import {PageModel} from "../../dto/security/page.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private host: string = 'http://localhost:8888/USER-SERVICE/bank/users';

  constructor(private http: HttpClient, private authService: AuthService) { }

  public create(request: UserRequest): Observable<UserResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/create';
    return this.http.post<UserResponse>(url, request, {headers: httpHeaders});
  }

  public update(id: string, request: UserRequest): Observable<UserResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/update/' + id;
    return this.http.put<UserResponse>(url, request, {headers: httpHeaders});
  }

  public addRoleToUser(request: UserRoleRequest): Observable<UserResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/add-role';
    return this.http.put<UserResponse>(url, request, {headers: httpHeaders});
  }

  public removeRoleFromUser(request: UserRoleRequest): Observable<UserResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/remove-role';
    return this.http.put<UserResponse>(url, request, {headers: httpHeaders});
  }

  public delete(id: string): Observable<void> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/delete/' +id;
    return this.http.delete<void>(url, {headers: httpHeaders});
  }

  public findById(id: string): Observable<UserResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/get/' +id;
    return this.http.get<UserResponse>(url, {headers: httpHeaders});
  }

  public findByUsername(): Observable<UserResponse> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/profile';
    return this.http.get<UserResponse>(url, {headers: httpHeaders});
  }

  public findAll(page: number, size: number): Observable<PageModel> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/all?page=' + page + '&size=' + size;
    return this.http.get<PageModel>(url, {headers: httpHeaders});
  }

  public search(keyword: string, page: number, size: number): Observable<PageModel> {
    const httpHeaders:HttpHeaders = this.authService.buildHttpHeaders();
    const url: string = this.host + '/search?keyword=' + keyword + '&page=' + page + '&size=' + size;
    return this.http.get<PageModel>(url, {headers: httpHeaders});
  }
}

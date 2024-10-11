import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthService} from "../auth/auth.service";
import {RoleRequest} from "../../models/role.request";
import {RoleResponse} from "../../models/role.response";
import {Observable} from "rxjs";
import {PageResponse} from "../../../lang/page.response";

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private readonly host: string = "http://localhost:8888/AUTHENTICATION-SERVICE/bank/roles"

  constructor(private readonly http: HttpClient, private readonly authService: AuthService) { }

  createRole(roleRequest: RoleRequest): Observable<RoleResponse> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.post<RoleResponse>(`${this.host}/create`, roleRequest, {
      headers
    });
  }

  updateRole(id: number, roleRequest: RoleRequest): Observable<RoleResponse> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.put<RoleResponse>(`${this.host}/update/${id}`, roleRequest, {
      headers
    });
  }

  deleteRoleById(id: number): Observable<void> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.delete<void>(`${this.host}/delete/${id}`, {
      headers
    });
  }

  getRoleById(id: number): Observable<RoleResponse> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.get<RoleResponse>(`${this.host}/get/${id}`, {
      headers
    });
  }

  getRoleByName(name: string): Observable<RoleResponse> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.get<RoleResponse>(`${this.host}/find/${name}`, {
      headers
    });
  }

  getAllRoles(page: number = 0, size: number = 9): Observable<PageResponse<RoleResponse>> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.get<PageResponse<RoleResponse>>(`${this.host}/all`, {
      headers,
      params: { page: page.toString(), size: size.toString() }
    });
  }

  searchRoles(query: string = '', page: number = 0, size: number = 9): Observable<PageResponse<RoleResponse>> {
    let headers: HttpHeaders = this.authService.getHttpHeaders();
    return this.http.get<PageResponse<RoleResponse>>(`${this.host}/search`, {
      headers,
      params: { query: query, page: page.toString(), size: size.toString() }
    });
  }

}

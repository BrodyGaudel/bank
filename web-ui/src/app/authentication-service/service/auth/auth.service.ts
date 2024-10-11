import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {JwtService} from "./jwt.service";
import {LoginRequest} from "../../models/login.request";
import {LoginResponse} from "../../models/login.response";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly host:string = "http://localhost:8888/AUTHENTICATION-SERVICE/bank";

  private readonly tokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  private readonly isLoggedInSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private readonly rolesSubject: BehaviorSubject<string[]> = new BehaviorSubject<string[]>([]);

  constructor(private readonly router: Router, private readonly http: HttpClient, private readonly jwtService: JwtService) {
    this.loadToken();
  }

  private loadToken(): void {
    const token: string | null = localStorage.getItem('jwt');
    if (token) {
      this.updateAuthenticationState(token);
    }
  }

  private updateAuthenticationState(jwt: string | null): void {
    if (jwt) {
      this.tokenSubject.next(jwt);
      this.isLoggedInSubject.next(true);
      this.decodeJWT(jwt);
    } else {
      this.tokenSubject.next(null);
      this.isLoggedInSubject.next(false);
      this.rolesSubject.next([]);
    }
  }

  private decodeJWT(jwt: string): void {
    const decodedToken = this.jwtService.decodeToken(jwt);
    this.rolesSubject.next(decodedToken.roles);
  }

  public isAdmin(): boolean {
    return this.rolesSubject.value.includes('ADMIN');
  }

  public isSuperAdmin(): boolean {
    return this.rolesSubject.value.includes('SUPER_ADMIN');
  }

  public getLoggedUser(): string | undefined {
    const token :string | null = this.tokenSubject.value;
    if (token) {
      const decodedToken = this.jwtService.decodeToken(token);
      return decodedToken.sub;
    }
    return undefined;
  }

  public getRoles(): Observable<string[]> {
    return this.rolesSubject.asObservable();
  }

  public getIsLoggedIn(): Observable<boolean> {
    this.loadToken();
    return this.isLoggedInSubject.asObservable();
  }

  public saveToken(jwt: string): void {
    localStorage.setItem('jwt', jwt);
    this.updateAuthenticationState(jwt);
  }

  public isTokenExpired(): boolean {
    const token :string | null = this.tokenSubject.value;
    return token ? this.jwtService.isTokenExpired(token) : true;
  }

  public getToken(): Observable<string | null> {
    return this.tokenSubject.asObservable();
  }

  public getHttpHeaders(): HttpHeaders {
    const token: string | null = this.tokenSubject.value;
    const authorization: string = token ? `Bearer ${token}` : '';
    return new HttpHeaders({
      'Authorization': authorization,
      'Content-Type': 'application/json'
    });
  }

  public login(request: LoginRequest): Observable<LoginResponse> {
    const url:string = this.host + '/authentication/login';
    return this.http.post<LoginResponse>(url, request);
  }

  public logout(): void {
    localStorage.removeItem('jwt');
    this.updateAuthenticationState(null);
    this.router.navigate(['/authentication']).then();
  }

  public security() :void{
    this.loadToken();
    if(this.getToken() == null || this.isTokenExpired()){
      this.logout();
    }
  }
}

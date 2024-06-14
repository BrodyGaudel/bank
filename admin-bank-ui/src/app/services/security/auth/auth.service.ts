import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {AuthenticationRequest} from "../../../dto/security/authentication.request";
import {AuthenticationResponse} from "../../../dto/security/authentication.response";
import {BehaviorSubject, Observable} from "rxjs";
import {JwtService} from "../jwt/jwt.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private url: string = 'http://localhost:8888/USER-SERVICE/bank/authentication/login';

  private tokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  private isLoggedInSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private rolesSubject: BehaviorSubject<string[]> = new BehaviorSubject<string[]>([]);

  constructor(private router: Router, private http: HttpClient, private jwtService: JwtService) {
    this.loadToken();
  }

  public getToken(): Observable<string | null> {
    return this.tokenSubject.asObservable();
  }

  public getLoggedUser(): string | undefined {
    const token = this.tokenSubject.value;
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
    return this.isLoggedInSubject.asObservable();
  }

  public saveToken(jwt: string): void {
    localStorage.setItem('jwt', jwt);
    this.updateAuthenticationState(jwt);
  }

  public isTokenExpired(): boolean {
    const token = this.tokenSubject.value;
    return token ? this.jwtService.isTokenExpired(token) : true;
  }

  public login(request: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(this.url, request);
  }

  public logout(): void {
    localStorage.removeItem('jwt');
    this.updateAuthenticationState(null);
    this.router.navigate(['/login']).then();
  }

  public security(): void {
    const token = this.tokenSubject.value;
    if (!token || this.isTokenExpired()) {
      this.logout();
    }
  }

  public isAdmin(): boolean {
    return this.rolesSubject.value.includes('ADMIN');
  }

  public isSuperAdmin(): boolean {
    return this.rolesSubject.value.includes('SUPER_ADMIN');
  }

  public isModerator(): boolean {
    return this.rolesSubject.value.includes('MODERATOR');
  }

  public buildHttpHeaders(): HttpHeaders {
    this.loadToken();
    const token = this.tokenSubject.value;
    let jwtToken: string = token ? `Bearer ${token}` : '';
    return new HttpHeaders({
      "Authorization": jwtToken,
      "Content-Type": "application/json"
    });
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

  private loadToken(): void {
    const token = localStorage.getItem('jwt');
    if (token) {
      this.updateAuthenticationState(token);
    }
  }

}

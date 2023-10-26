import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {JwtHelperService} from "@auth0/angular-jwt";
import {AuthenticationRequest} from "../../models/authenticationRequest";
import {AuthenticationResponse} from "../../models/authenticationResponse";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private helper: JwtHelperService = new JwtHelperService();

  private host: string = 'http://localhost:8888/AUTHENTICATION-SERVICE/bank';
  private token!: string;

  private loggedUser!: string;
  private isLoggedIn: Boolean = false;
  private roles!: string[];

  constructor(private http : HttpClient, private router: Router) { }

  public login(request: AuthenticationRequest) :Observable<AuthenticationResponse>  {
    return this.http.post<AuthenticationResponse>(this.host +'/login', request );
  }

  public logout() :void {
    this.loggedUser = undefined!;
    this.roles = undefined!;
    this.token= undefined!;
    this.isLoggedIn = false;
    localStorage.removeItem('jwt');
    this.router.navigate(['/login']).then();
  }

  public saveToken(jwt: string) :void{
    localStorage.setItem('jwt',jwt);
    this.token = jwt;
    this.isLoggedIn = true;
    this.decodeJWT();
  }

  public isAdmin() :Boolean{
    if(!this.roles){
      return false;
    }
    return (this.roles.indexOf('ADMIN') >-1);
  }

  public getLoggedUser() :string{
    return this.loggedUser;
  }

  public checkIfIsLoggedIn() :Boolean{
    return this.isLoggedIn;
  }

  public isTokenExpired(): Boolean{
    return  this.helper.isTokenExpired(this.token);
  }

  getToken() :string {
    return this.token;
  }


  private decodeJWT() :void {
    if(this.token == undefined){
      return;
    }
    const decodedToken = this.helper.decodeToken(this.token);
    this.roles = decodedToken.roles;
    this.loggedUser = decodedToken.sub;
  }






  setLoggedUserFromLocalStorage(login: string) {
    this.loggedUser = login;
    this.isLoggedIn = true;
  }

  loadToken() {
    this.token = localStorage.getItem('jwt')!;
    this.decodeJWT();
  }



}

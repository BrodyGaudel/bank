import { Injectable } from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {UserModel} from "../../models/user.model";
import {JwtHelperService} from "@auth0/angular-jwt";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private helper : JwtHelperService = new JwtHelperService();

  private host: string = 'http://localhost:8888/USER-SERVICE/bank/login';
  token!: string;

  public loggedUser!: string;
  public isloggedIn: Boolean = false;
  public roles!: string[];

  constructor(private router : Router,
              private http : HttpClient) { }

  login(user : UserModel): Observable<HttpResponse<UserModel>> {
    return this.http.post<UserModel>(this.host, user , {observe:'response'});
  }

  saveToken(jwt:string) : void {
    localStorage.setItem('jwt',jwt);
    this.token = jwt;
    this.isloggedIn = true;
    this.decodeJWT();
  }

  decodeJWT() : void {
    if(this.token == undefined){
      return;
    }
    const decodedToken = this.helper.decodeToken(this.token);
    this.roles = decodedToken.roles;
    this.loggedUser = decodedToken.sub;
  }

  isAdmin():Boolean{
    if(!this.roles){
      return false;
    }
    return (this.roles.indexOf('ADMIN') >-1);
  }

  logout() : void {
    this.loggedUser = undefined!;
    this.roles = undefined!;
    this.token= undefined!;
    this.isloggedIn = false;
    localStorage.removeItem('jwt');
    this.router.navigate(['/login']).then();
  }

  setLoggedUserFromLocalStorage(login: string) : void {
    this.loggedUser = login;
    this.isloggedIn = true;
  }

  loadToken() {
    this.token = localStorage.getItem('jwt')!;
    this.decodeJWT();
  }

  isTokenExpired(): Boolean{
    return  this.helper.isTokenExpired(this.token);
  }

  security(): void{
    if(!this.isloggedIn){
      this.logout();
    }
  }

  getToken() :string {
    return this.token;
  }

}

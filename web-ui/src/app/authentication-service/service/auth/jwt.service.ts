import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  private readonly jwtHelperService: JwtHelperService = new JwtHelperService();

  constructor() { }

  public decodeToken(token: string): any {
    return this.jwtHelperService.decodeToken(token);
  }

  public isTokenExpired(token: string): boolean {
    return this.jwtHelperService.isTokenExpired(token);
  }
}

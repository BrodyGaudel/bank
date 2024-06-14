import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../security/auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private host: string = 'http://localhost:8888/USER-SERVICE/bank/users';

  constructor(private http: HttpClient, private authService: AuthService) { }
}

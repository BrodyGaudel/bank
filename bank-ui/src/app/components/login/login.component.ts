import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/security/auth/auth.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationRequest} from "../../models/request/authentication.request";
import {AuthenticationResponse} from "../../models/response/authentication.response";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  loginFormGroup!: FormGroup;
  errorFlag: boolean = false;
  errorMessage: string = 'Password or username is incorrect.';

  constructor(private fb: FormBuilder,
              private router: Router,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.loginFormGroup = this.fb.group({
      username: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      password: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
    });
  }

  handleLogin(): void {
    this.errorFlag = false;
    const request: AuthenticationRequest = this.loginFormGroup.value;
    this.authService.login(request).subscribe({
      next: result => {
        this.handleLoginSuccess(result);
      },
      error: err => {
        this.handleLoginFailure(err);
      }
    });
  }

  private handleLoginSuccess(result: AuthenticationResponse) :void {
    if(result.passwordNeedToBeModified){
      this.authService.saveToken(result.jwt);
      this.router.navigate(['/home']).then();
    }else{
      this.errorMessage = "You need to reset your password or contact an admin";
      this.errorFlag = true;
    }
  }

  private handleLoginFailure(err: any) :void {
    console.log(err);
    this.errorFlag = true;
  }
}

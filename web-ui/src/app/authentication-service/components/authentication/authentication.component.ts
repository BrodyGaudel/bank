import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth/auth.service";
import {Router} from "@angular/router";
import {LoginRequest} from "../../models/login.request";
import {LoginResponse} from "../../models/login.response";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-authentication',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './authentication.component.html',
  styleUrl: './authentication.component.css'
})
export class AuthenticationComponent implements OnInit {

  loginFormGroup!: FormGroup;
  errorFlag: boolean = false;
  errorMessage: string = 'Password or username is incorrect.';

  constructor(private readonly fb: FormBuilder,
              private readonly router: Router,
              private readonly authService: AuthService) {
  }

  ngOnInit(): void {
    this.loginFormGroup = this.fb.group({
      username: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      password: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
    });
  }

  handleLogin(): void {
    this.errorFlag = false;
    const request: LoginRequest = this.loginFormGroup.value;
    this.authService.login(request).subscribe({
      next: result => {
        this.handleLoginSuccess(result);
      },
      error: err => {
        this.handleLoginFailure(err);
      }
    });
  }

  private handleLoginSuccess(result: LoginResponse) :void {
    this.authService.saveToken(result.jwt);
    if(result.passwordNeedToBeUpdate){
      this.router.navigate(['/user-update-password']).then();
    }else{
      this.router.navigate(['/home']).then();
    }
  }

  private handleLoginFailure(err: any) :void {
    console.log(err);
    this.errorFlag = true;
  }

  gotoRequestCodeComponent() :void {
    this.router.navigate(["user-request-code"]).then()
  }
}

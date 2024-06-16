import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/security/auth/auth.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationRequest} from "../../dto/security/authentication.request";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginFormGroup!: FormGroup;
  errorFlag: boolean = false;
  errorMessage: string = "Nom d'utilisateur ou mot de passe incorrecte!";

  constructor(private authService: AuthService,
              private router: Router,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.loginFormGroup = this.formBuilder.group( {
      usernameOrEmail : this.formBuilder.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      password : this.formBuilder.control(null, [Validators.required, Validators.maxLength(256), Validators.minLength(1)]),
    });
    this.authService.reset();
  }

  handleLogin(): void {
    this.errorFlag = false;
    let request: AuthenticationRequest = new AuthenticationRequest();
    request.usernameOrEmail = this.loginFormGroup.value.usernameOrEmail;
    request.password = this.loginFormGroup.value.password;

    this.authService.login(request).subscribe({
      next : data  => {
        this.authService.saveToken(data.jwt);
        this.router.navigate(["all-operations"]).then();
      },
      error : err => {
        this.errorFlag = true;
        console.log(err)
      }
    });
  }

}

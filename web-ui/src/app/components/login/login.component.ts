import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth-service/auth.service";
import {Router} from "@angular/router";
import {AuthenticationRequest} from "../../models/authenticationRequest";
import {AuthenticationResponse} from "../../models/authenticationResponse";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  loginFormGroup!: FormGroup;
  errorFlag:number = 0;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.loginFormGroup = this.fb.group( {
      username : this.fb.control(null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)]),
      password : this.fb.control( null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)])
    });
  }

  handleLogin() :void {
    let request : AuthenticationRequest = new AuthenticationRequest();
    request.username = this.loginFormGroup.value.username;
    request.password = this.loginFormGroup.value.password;

    this.authService.login(request).subscribe({
      next: (data : AuthenticationResponse) :void => {
        let jwToken: string = data.token;
        this.authService.saveToken(jwToken);
        this.router.navigate(['/']).then(() => {});
      },
      error: (err: any) => {
        this.errorFlag = 1;
        console.log(err.message);
      }
    });
  }
}

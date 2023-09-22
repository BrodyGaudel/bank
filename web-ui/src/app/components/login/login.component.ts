import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {UserModel} from "../../models/user.model";
import {AuthService} from "../../services/auth-service/auth.service";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  loginFormGroup!: FormGroup;
  errorFlag:number = 0;

  constructor(private fb: FormBuilder, private securityService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.loginFormGroup = this.fb.group( {
      username : this.fb.control(null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)]),
      password : this.fb.control( null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)])
    });
  }

  handleLogin() {
    let user : UserModel = new UserModel();
    user.password = this.loginFormGroup.value.password;
    user.username = this.loginFormGroup.value.username;
    this.securityService.login(user).subscribe({
      next: (data : HttpResponse<UserModel>) : void => {
        let jwToken : string = data.headers.get('Authorization')!;
        this.securityService.saveToken(jwToken);
        this.router.navigate(['/']).then(() : void => {});
      },
      error: (err: any) : void => {
        this.errorFlag = 1;
        console.log(err.message)
      }
    });
  }

}

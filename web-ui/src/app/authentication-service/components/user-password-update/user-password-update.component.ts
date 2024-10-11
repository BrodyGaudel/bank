import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ErrorHandlerService} from "../../../exception/error-handler.service";
import {Router} from "@angular/router";
import {RoleService} from "../../service/role/role.service";
import {NgbAlert, NgbAlertConfig} from "@ng-bootstrap/ng-bootstrap";
import {UserService} from "../../service/user/user.service";
import {UserUpdatePasswordRequest} from "../../models/user-update-password.request";
import {UserResponse} from "../../models/user.response";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-user-password-update',
  standalone: true,
  imports: [
    NgbAlert,
    ReactiveFormsModule,
    NgIf,
    NgForOf
  ],
  templateUrl: './user-password-update.component.html',
  styleUrl: './user-password-update.component.css'
})
export class UserPasswordUpdateComponent implements OnInit{

  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];
  updatePasswordFormGroup!: FormGroup;

  constructor(private readonly errorHandlerService: ErrorHandlerService,
              private readonly router: Router,
              private readonly userService: UserService,
              private readonly fb: FormBuilder,
              alertConfig: NgbAlertConfig) {

    alertConfig.type = 'warning';
    alertConfig.dismissible = false;
  }

  ngOnInit(): void {
    this.initUpdatePasswordFormGroup();
  }

  private initUpdatePasswordFormGroup() :void {
    this.updatePasswordFormGroup = this.fb.group({
      oldPassword: this.fb.control(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(256)
      ]),

      newPassword: this.fb.control(null, [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(256)
      ]),

      confirmPassword: this.fb.control(null, [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(256)
      ]),
    });
  }

  handleSubmit(): void {
    let request: UserUpdatePasswordRequest = this.updatePasswordFormGroup.value;
    if (this.updatePasswordFormGroup.get('newPassword')?.value === this.updatePasswordFormGroup.get('confirmPassword')?.value) {
      this.userService.updatePassword(request).subscribe({
        next: result => {
          this.handleUpdateSuccess(result);
        },
        error: err => {
          this.handleUpdateFailure(err);
        }
      });
    } else {
      alert("Password not match");
      this.updatePasswordFormGroup.reset();
    }


  }


  private handleUpdateSuccess(result: UserResponse) :void {
    this.router.navigate(['/user', result.id]).then();
  }

  private handleUpdateFailure(err: any) {
    console.log(err);
    this.errorHandlerService.handleError(err);
    this.subscribeToErrors();
    this.errorFlag = true;
  }

  private subscribeToErrors(): void {
    this.errorHandlerService.error$.subscribe(error => {
      if (error) {
        this.errorMessage = error.message;
        this.errorCode = error.code;
        this.errorValidation = error.validationErrors;
        this.errorFlag = true;
      } else {
        this.errorFlag = false;
      }
    });
  }
}

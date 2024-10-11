import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ErrorHandlerService} from "../../../exception/error-handler.service";
import {Router} from "@angular/router";
import {PasswordService} from "../../service/password/password.service";
import {NgbAlert, NgbAlertConfig} from "@ng-bootstrap/ng-bootstrap";
import {UserResetPasswordRequest} from "../../models/user-reset-password.request";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-user-reset-password',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    NgbAlert,
    ReactiveFormsModule
  ],
  templateUrl: './user-reset-password.component.html',
  styleUrl: './user-reset-password.component.css'
})
export class UserResetPasswordComponent implements OnInit{

  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];
  resetFormGroup!: FormGroup;

  constructor(private readonly errorHandlerService: ErrorHandlerService,
              private readonly router: Router,
              private readonly passwordService: PasswordService,
              private readonly fb: FormBuilder,
              alertConfig: NgbAlertConfig) {

    alertConfig.type = 'warning';
    alertConfig.dismissible = false;
  }

  ngOnInit(): void {
    this.initResetFormGroup();
  }

  handleSubmit(): void {
    let request: UserResetPasswordRequest = this.resetFormGroup.value;
    this.passwordService.submit(request).subscribe({
      next: result => {
        this.handleSuccess();
      },
      error: err => {
        this.handleFailure(err);
      }
    });
  }

  initResetFormGroup(): void {
    this.resetFormGroup = this.fb.group({
      email: this.fb.control(null, [
        Validators.required,
        Validators.email,
        Validators.minLength(4),
        Validators.maxLength(256)
      ]),
      code: this.fb.control(null, [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(6)
      ]),
      password: this.fb.control(null, [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(64)
      ]),
      confirmPassword: this.fb.control(null, [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(64)
      ]),
    });
  }

  private handleSuccess(): void {
    this.router.navigate(['/authentication']).then();
  }

  private handleFailure(err: any) {
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

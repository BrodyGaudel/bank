import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ErrorHandlerService} from "../../../exception/error-handler.service";
import {Router} from "@angular/router";
import {UserService} from "../../service/user/user.service";
import {UserRequest} from "../../models/user.request";
import {UserResponse} from "../../models/user.response";
import {NgForOf, NgIf} from "@angular/common";
import {NgbAlertConfig, NgbAlertModule} from "@ng-bootstrap/ng-bootstrap";
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    ReactiveFormsModule,
    NgbAlertModule
  ],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.css'
})
export class UserCreateComponent implements OnInit {

  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];
  createUserFormGroup!: FormGroup;

  constructor(private readonly errorHandlerService: ErrorHandlerService,
              private readonly router: Router,
              private readonly userService: UserService,
              private readonly fb: FormBuilder,
              alertConfig: NgbAlertConfig,
              private readonly authService: AuthService) {

    alertConfig.type = 'warning';
    alertConfig.dismissible = false;
  }

  ngOnInit(): void {
    this.authService.security();
    this.initCreateUserFormGroup();
  }

  handleSubmit(): void {
    let request: UserRequest = this.createUserFormGroup.value;
    this.userService.create(request).subscribe({
      next: result => {
        this.handleCreationSuccess(result);
      },
      error: err => {
        this.handleCreationFailure(err);
      }
    });
  }

  initCreateUserFormGroup(): void {
    this.createUserFormGroup = this.fb.group({
      firstname: this.fb.control(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(256)
      ]),

      lastname: this.fb.control(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(256)
      ]),

      dateOfBirth: this.fb.control(null, [
        Validators.required,
        Validators.pattern(/^\d{4}-\d{2}-\d{2}$/) // Validation d'une date au format 'YYYY-MM-DD'
      ]),

      placeOfBirth: this.fb.control(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(256)
      ]),

      gender: this.fb.control(null, [
        Validators.required,
        Validators.pattern(/^([MF])$/) // Valide 'Male', 'Female'
      ]),

      nationality: this.fb.control(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(256)
      ]),

      cin: this.fb.control(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(256)
      ]),

      email: this.fb.control(null, [
        Validators.required,
        Validators.email,
        Validators.maxLength(256)
      ]),

      username: this.fb.control(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(256)
      ]),

      password: this.fb.control(null, [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(256),
        Validators.pattern(/^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]+$/) // Doit contenir au moins une lettre et un chiffre
      ])
    });

  }

  private handleCreationSuccess(result: UserResponse): void {
    this.router.navigate(['/user', result.id]).then();
  }

  private handleCreationFailure(err: any) {
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

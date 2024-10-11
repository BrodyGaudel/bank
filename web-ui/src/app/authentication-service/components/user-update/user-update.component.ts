import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {ErrorHandlerService} from "../../../exception/error-handler.service";
import {UserService} from "../../service/user/user.service";
import {UserResponse} from "../../models/user.response";
import {UserRequest} from "../../models/user.request";
import {NgForOf, NgIf} from "@angular/common";
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'app-user-update',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgForOf
  ],
  templateUrl: './user-update.component.html',
  styleUrl: './user-update.component.css'
})
export class UserUpdateComponent implements OnInit{

  updateUserFormGroup!: FormGroup;
  userId!: string;
  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];

  constructor(private readonly fb: FormBuilder,
              private readonly route: ActivatedRoute,
              private readonly router: Router,
              private readonly userService: UserService,
              private readonly errorHandlerService: ErrorHandlerService,
              private readonly authService: AuthService) {}

  ngOnInit(): void {
    this.authService.security();
    this.userId = this.route.snapshot.paramMap.get('id')!;
    this.initUpdateCustomerFormGroup();
    this.loadUserData();
  }

  initUpdateCustomerFormGroup(): void {
    this.updateUserFormGroup = this.fb.group({
      firstname: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      lastname: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      placeOfBirth: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      dateOfBirth: this.fb.control(null, [Validators.required]),
      nationality: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      gender: this.fb.control(null, [Validators.required]),
      cin: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      email: this.fb.control(null, [Validators.required, Validators.email, Validators.maxLength(256)]),
      username: this.fb.control(null, [Validators.required, Validators.minLength(4), Validators.maxLength(256)]),
    });
  }

  loadUserData(): void {
    this.userService.getById(this.userId).subscribe({
      next: (user: UserResponse) => {
        this.updateUserFormGroup.patchValue(user);
      },
      error: (err) => {
        console.error(err);
        this.errorHandlerService.handleError(err);
      }
    });
  }

  handleSubmit(): void {
    if (this.updateUserFormGroup.valid) {
      let request: UserRequest = this.updateUserFormGroup.value;
      this.userService.update(this.userId, request).subscribe({
        next: (result : UserResponse) :void => {
          this.handleUpdateSuccess(result);
        },
        error: (err) :void => {
          this.handleUpdateFailure(err);
        }
      });
    }
  }

  private handleUpdateSuccess(result: UserResponse): void {
    this.router.navigate(['/user', result.id]).then();
  }

  private handleUpdateFailure(err: any): void {
    console.log(err);
    this.errorHandlerService.handleError(err);
    this.subscribeToErrors();
    this.errorFlag = true;
  }

  private subscribeToErrors(): void {
    this.errorHandlerService.error$.subscribe((error) => {
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

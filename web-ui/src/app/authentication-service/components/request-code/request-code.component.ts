import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ErrorHandlerService} from "../../../exception/error-handler.service";
import {Router} from "@angular/router";
import {NgbAlert, NgbAlertConfig} from "@ng-bootstrap/ng-bootstrap";
import {PasswordService} from "../../service/password/password.service";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-request-code',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgForOf,
    NgIf,
    NgbAlert
  ],
  templateUrl: './request-code.component.html',
  styleUrl: './request-code.component.css'
})
export class RequestCodeComponent implements OnInit{


  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];
  emailFormGroup!: FormGroup;

  constructor(private readonly errorHandlerService: ErrorHandlerService,
              private readonly router: Router,
              private readonly passwordService: PasswordService,
              private readonly fb: FormBuilder,
              alertConfig: NgbAlertConfig) {

    alertConfig.type = 'warning';
    alertConfig.dismissible = false;
  }

  ngOnInit(): void {
    this.initEmailFormGroup();
  }

  handleSubmit(): void {
    let request: string = this.emailFormGroup.value.email;
    this.passwordService.ask(request).subscribe({
      next: result => {
        this.handleCreationSuccess();
      },
      error: err => {
        this.handleCreationFailure(err);
      }
    });
  }

  initEmailFormGroup(): void {
    this.emailFormGroup = this.fb.group({
      email: this.fb.control(null, [
        Validators.required,
        Validators.email,
        Validators.minLength(4),
        Validators.maxLength(256)
      ]),
    });

  }

  private handleCreationSuccess(): void {
    this.router.navigate(['/user-reset-password']).then();
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

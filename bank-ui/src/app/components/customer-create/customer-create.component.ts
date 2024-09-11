import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {CustomerService} from "../../services/customers/customer.service";
import {CustomerRequest} from "../../models/request/customer.request";
import {CustomerResponse} from "../../models/response/customer.response";
import {ErrorHandlerService} from "../../services/exception/error-handler.service";
import {AccountService} from "../../services/accounts/account.service";
import {Observable} from "rxjs";
import {AccountRequest} from "../../models/request/account.request";

@Component({
  selector: 'app-customer-create',
  templateUrl: './customer-create.component.html',
  styleUrl: './customer-create.component.css'
})
export class CustomerCreateComponent implements OnInit {

  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];
  createCustomerFormGroup!: FormGroup;

  constructor(private errorHandlerService: ErrorHandlerService,
              private router: Router,
              private customerService: CustomerService,
              private fb: FormBuilder,
              private accountService: AccountService) {
  }

  ngOnInit(): void {
    this.initCreateCustomerFormGroup();
  }

  initCreateCustomerFormGroup(): void {
    this.createCustomerFormGroup = this.fb.group({
      firstname: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      lastname: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      placeOfBirth: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      dateOfBirth: this.fb.control(null, [Validators.required]),
      nationality: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      gender: this.fb.control(null, [Validators.required]),
      cin: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      email: this.fb.control(null, [Validators.required, Validators.email, Validators.maxLength(256)]),
      currency: this.fb.control(null, [Validators.required, Validators.minLength(3), Validators.maxLength(3)]),
    });
  }

  handleSubmit(): void {
    let request: CustomerRequest = this.createCustomerFormGroup.value;
    this.customerService.createCustomer(request).subscribe({
      next: result => {
        this.handleCreationSuccess(result);
      },
      error: err => {
        this.handleCreationFailure(err);
      }
    });
  }


  private handleCreationSuccess(result: CustomerResponse) :void {
    this.createAccount(result.id);
    this.router.navigate(['/user', result.id]).then();
  }

  private handleCreationFailure(err: any) :void {
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

  private createAccount(customerId: string): void{
    let request: AccountRequest = new AccountRequest();
    request.customerId = customerId;
    request.currency = this.createCustomerFormGroup.value.currency;

    this.accountService.createAccount(request).subscribe({
      next: result => {
        console.log(result);
        this.router.navigate(["customer-get", customerId]).then();
      },
      error: err => {
        console.log(err);
        this.router.navigate(["customer-get", customerId]).then();
      }
    })
  }
}

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {CustomerService} from "../../services/customers/customer.service";
import {ErrorHandlerService} from "../../services/exception/error-handler.service";
import {CustomerResponse} from "../../models/response/customer.response";
import {CustomerRequest} from "../../models/request/customer.request";

@Component({
  selector: 'app-customer-update',
  templateUrl: './customer-update.component.html',
  styleUrl: './customer-update.component.css'
})
export class CustomerUpdateComponent implements OnInit {

  updateCustomerFormGroup!: FormGroup;
  customerId!: string;
  errorFlag: boolean = false;
  errorMessage!: string;
  errorCode!: number;
  errorValidation!: [];

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private customerService: CustomerService,
              private errorHandlerService: ErrorHandlerService) {}

  ngOnInit(): void {
    this.customerId = this.route.snapshot.paramMap.get('id')!;
    this.initUpdateCustomerFormGroup();
    this.loadCustomerData();
  }

  initUpdateCustomerFormGroup(): void {
    this.updateCustomerFormGroup = this.fb.group({
      firstname: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      lastname: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      placeOfBirth: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      dateOfBirth: this.fb.control(null, [Validators.required]),
      nationality: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      gender: this.fb.control(null, [Validators.required]),
      cin: this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      email: this.fb.control(null, [Validators.required, Validators.email, Validators.maxLength(256)]),
    });
  }

  loadCustomerData(): void {
    this.customerService.getCustomerById(this.customerId).subscribe({
      next: (customer: CustomerResponse) => {
        this.updateCustomerFormGroup.patchValue(customer);
      },
      error: (err) => {
        console.error(err);
        this.errorHandlerService.handleError(err);
      }
    });
  }

  handleSubmit(): void {
    if (this.updateCustomerFormGroup.valid) {
      let request: CustomerRequest = this.updateCustomerFormGroup.value;
      this.customerService.updateCustomer(this.customerId, request).subscribe({
        next: (result) => {
          this.handleUpdateSuccess(result);
        },
        error: (err) => {
          this.handleUpdateFailure(err);
        }
      });
    }
  }

  private handleUpdateSuccess(result: CustomerResponse): void {
    this.router.navigate(['/customer-get', result.id]).then();
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

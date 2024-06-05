import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../services/customer/customer.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CustomerResponse} from "../../dto/customer.response";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CustomerRequest} from "../../dto/customer.request";


@Component({
  selector: 'app-update-customer',
  templateUrl: './update-customer.component.html',
  styleUrl: './update-customer.component.css'
})
export class UpdateCustomerComponent implements OnInit {

  customer: CustomerResponse = new CustomerResponse();
  customerId!: string;
  errorMessage!: string;
  errorDescription!: string;
  errorCode!: number;
  errorFlag: number = 0;
  updateCustomerFormGroup!: FormGroup;

  constructor(private customerService: CustomerService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.customerId = this.activatedRoute.snapshot.params['id'];
    this.initializeCreateCustomerFormGroup();
    this.getCustomerById(this.customerId);
  }

  private getCustomerById(id: string):void {
    this.errorFlag = 0;
    this.customerService.getCustomerById(id).subscribe({
        next : data  => {
          this.customer = data;
          this.addDefaultValueToCreateCustomerFormGroup();
        },
        error : err => {
          this.errorFlag = 1;
          console.log(err);
          this.handleError(err);
        }
    });
  }

  private initializeCreateCustomerFormGroup() :void{
    this.updateCustomerFormGroup = this.fb.group( {
      name : this.fb.control(null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      nationality : this.fb.control(null, [Validators.required, Validators.maxLength(256), Validators.minLength(2)]),
      dateOfBirth : this.fb.control(null, [Validators.required]),
      placeOfBirth : this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      sex : this.fb.control(null, [Validators.required]),
      firstname : this.fb.control( null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      email : this.fb.control(null, [Validators.required, Validators.email, Validators.minLength(3), Validators.maxLength(256)]),
      cin : this.fb.control(null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)])
    });
  }

  private addDefaultValueToCreateCustomerFormGroup() :void{
    this.updateCustomerFormGroup.patchValue({
      name: this.customer.lastname,
      nationality: this.customer.nationality,
      dateOfBirth: this.customer.dateOfBirth,
      placeOfBirth: this.customer.placeOfBirth,
      sex: this.customer.sex,
      firstname: this.customer.firstname,
      email: this.customer.email,
      cin: this.customer.cin
    });
  }

  private handleError(error: any) :void {
    if (error.error instanceof ErrorEvent) {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error.message;
      this.errorCode = error.error.code;
      this.errorDescription = error.error.description;
    }
  }

  private buildCustomerFormGroup() :CustomerRequest{
    let model : CustomerRequest = new CustomerRequest();
    model.email = this.updateCustomerFormGroup.value.email;
    model.lastname = this.updateCustomerFormGroup.value.name;
    model.firstname = this.updateCustomerFormGroup.value.firstname;
    model.email = this.updateCustomerFormGroup.value.email;
    model.nationality = this.updateCustomerFormGroup.value.nationality;
    model.sex = this.updateCustomerFormGroup.value.sex;
    model.cin = this.updateCustomerFormGroup.value.cin;
    model.dateOfBirth = this.updateCustomerFormGroup.value.dateOfBirth;
    model.placeOfBirth = this.updateCustomerFormGroup.value.placeOfBirth;
    return model;
  }


  handleUpdateCustomer() :void {
    let request: CustomerRequest = this.buildCustomerFormGroup();
    this.customerService.updateCustomer(this.customerId, request).subscribe({
      next : data  => {
        this.customer = data;
        this.router.navigate(["customers-details", data.id]).then();
      },
      error : err => {
        this.errorFlag = 1;
        console.log(err);
        this.handleError(err);
      }
    })
  }
}

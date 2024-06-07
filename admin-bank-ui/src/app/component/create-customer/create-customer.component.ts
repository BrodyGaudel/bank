import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CustomerService} from "../../services/customer/customer.service";
import {Router} from "@angular/router";
import {CustomerRequest} from "../../dto/customer/customer.request";

@Component({
  selector: 'app-create-customer',
  templateUrl: './create-customer.component.html',
  styleUrl: './create-customer.component.css'
})
export class CreateCustomerComponent implements OnInit{

  createCustomerFormGroup!: FormGroup;

  constructor(private fb: FormBuilder,
              private customerService: CustomerService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.initializeCreateCustomerFormGroup();
  }

  handleCreateCustomer(): void {
    let customer: CustomerRequest = this.buildCustomerFormGroup();
    this.customerService.createCustomer(customer).subscribe({
      next : data  => {
        alert("Customer successfully saved");
        alert(data.userId)
        this.router.navigate(["customers-details", data.id]).then();
      },
      error : err => {
        console.log(err)
        alert("Customer not saved due to : "+err.message);
      }
    });
  }

  private initializeCreateCustomerFormGroup() :void{
    this.createCustomerFormGroup = this.fb.group( {
      name : this.fb.control(null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      firstname : this.fb.control( null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      email : this.fb.control(null, [Validators.required, Validators.email, Validators.minLength(3), Validators.maxLength(256)]),
      nationality : this.fb.control(null, [Validators.required, Validators.maxLength(256), Validators.minLength(2)]),
      dateOfBirth : this.fb.control(null, [Validators.required]),
      placeOfBirth : this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      sex : this.fb.control(null, [Validators.required]),
      cin : this.fb.control(null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)])
    });
  }

  private buildCustomerFormGroup() :CustomerRequest{
    let model : CustomerRequest = new CustomerRequest();
    model.email = this.createCustomerFormGroup.value.email;
    model.lastname = this.createCustomerFormGroup.value.name;
    model.firstname = this.createCustomerFormGroup.value.firstname;
    model.email = this.createCustomerFormGroup.value.email;
    model.nationality = this.createCustomerFormGroup.value.nationality;
    model.sex = this.createCustomerFormGroup.value.sex;
    model.cin = this.createCustomerFormGroup.value.cin;
    model.dateOfBirth = this.createCustomerFormGroup.value.dateOfBirth;
    model.placeOfBirth = this.createCustomerFormGroup.value.placeOfBirth;
    return model;
  }

}

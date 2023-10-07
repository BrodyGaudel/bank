import {Component, OnInit} from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {CustomerModel} from "../../models/customer.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CustomerService} from "../../services/customer-service/customer.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-customer',
  templateUrl: './create-customer.component.html',
  styleUrls: ['./create-customer.component.css']
})
export class CreateCustomerComponent implements OnInit{

  customerSaved!: Observable<CustomerModel>;
  addCustomerFormGroup!: FormGroup;


  constructor(private customerService: CustomerService,
              private router: Router,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.initFormGroup();
  }

  initFormGroup(): void{
    this.addCustomerFormGroup = this.fb.group( {
      name : this.fb.control(null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      cin : this.fb.control(null, [Validators.required, Validators.maxLength(256), Validators.minLength(6)]),
      sex : this.fb.control(null, [Validators.required]),
      firstname : this.fb.control( null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      nationality : this.fb.control(null, [Validators.required, Validators.minLength(3), Validators.maxLength(256)]),
      dateOfBirth : this.fb.control(null, [Validators.required]),
      placeOfBirth : this.fb.control(null, [Validators.required, Validators.maxLength(256), Validators.minLength(2)]),
      email : this.fb.control(null, [Validators.required, Validators.email]),
      phone : this.fb.control(null, [Validators.required, Validators.maxLength(16), Validators.minLength(6)])
    });
  }

  addCustomer() : void {
    let model: CustomerModel = new CustomerModel();
    model.sex = this.addCustomerFormGroup.value.sex;
    model.firstname = this.addCustomerFormGroup.value.firstname;
    model.name = this.addCustomerFormGroup.value.name;
    model.cin = this.addCustomerFormGroup.value.cin;
    model.placeOfBirth = this.addCustomerFormGroup.value.placeOfBirth;
    model.dateOfBirth = this.addCustomerFormGroup.value.dateOfBirth;
    model.nationality = this.addCustomerFormGroup.value.nationality;
    model.email = this.addCustomerFormGroup.value.email;
    model.phone = this.addCustomerFormGroup.value.phone;
    this.customerSaved = this.customerService.createCustomer(model).pipe(
      catchError(err => {
        alert(err.message);
        return throwError(() => err);
      })
    );
    this.customerSaved.subscribe(
      c => {
        this.router.navigate(['show-customer', c.id]).then();
      }
    );
  }


}

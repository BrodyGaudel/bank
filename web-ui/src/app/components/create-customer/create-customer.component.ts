import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {CustomerService} from "../../services/customer-service/customer.service";
import {CustomerModel} from "../../models/customer.model";

@Component({
  selector: 'app-create-customer',
  templateUrl: './create-customer.component.html',
  styleUrls: ['./create-customer.component.css']
})
export class CreateCustomerComponent implements OnInit{

  newCustomerFormGroup!: FormGroup;

  constructor(private fb: FormBuilder,
              private service: CustomerService,
              private router: Router) { }

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group( {
      name : this.fb.control(null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      firstname : this.fb.control( null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      email : this.fb.control(null, [Validators.required, Validators.email, Validators.minLength(3), Validators.maxLength(256)]),
      phone : this.fb.control(null, [Validators.required, Validators.maxLength(16), Validators.minLength(6)]),
      nationality : this.fb.control(null, [Validators.required, Validators.maxLength(256), Validators.minLength(2)]),
      dateOfBirth : this.fb.control(null, [Validators.required]),
      placeOfBirth : this.fb.control(null, [Validators.required, Validators.minLength(1), Validators.maxLength(256)]),
      sex : this.fb.control(null, [Validators.required]),
      cin : this.fb.control(null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)])
    });
  }

  saveCustomer() :void{
    let model : CustomerModel = new CustomerModel();
    model.email = this.newCustomerFormGroup.value.email;
    model.name = this.newCustomerFormGroup.value.name;
    model.firstname = this.newCustomerFormGroup.value.firstname;
    model.phone = this.newCustomerFormGroup.value.phone;
    model.nationality = this.newCustomerFormGroup.value.nationality;
    model.sex = this.newCustomerFormGroup.value.sex;
    model.cin = this.newCustomerFormGroup.value.cin;
    model.dateOfBirth = this.newCustomerFormGroup.value.dateOfBirth;
    model.placeOfBirth = this.newCustomerFormGroup.value.placeOfBirth;

    this.service.save(model).subscribe({
      next : data  => {
        alert("Customer successfully saved");
        this.router.navigate(["show-customer", data.id]).then();
      },
      error : err => {
        console.log(err)
        alert("Customer not saved due to : "+err.message);
      }
    });
  }

}

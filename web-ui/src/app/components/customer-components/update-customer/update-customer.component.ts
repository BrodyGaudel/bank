import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../../services/customer-service/customer.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CustomerModel} from "../../../models/customer.model";

@Component({
  selector: 'app-update-customer',
  templateUrl: './update-customer.component.html',
  styleUrls: ['./update-customer.component.css']
})
export class UpdateCustomerComponent implements OnInit {

  id!: string;
  customer : CustomerModel = new CustomerModel();
  updateCustomerFormGroup!: FormGroup;

  constructor(private customerService: CustomerService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private fb: FormBuilder) {
  }
  ngOnInit(): void {
    this.initUpdateCustomerFormGroup();
    this.id = this.activatedRoute.snapshot.params['id'];
    this.customerService.getCustomerById(this.id).subscribe(
      c => {
        this.customer = c;
      }
    );
  }

  initUpdateCustomerFormGroup(): void{
    this.updateCustomerFormGroup = this.fb.group( {
      nationality : this.fb.control(null, [Validators.required, Validators.minLength(3), Validators.maxLength(256)]),
      dateOfBirth : this.fb.control(null, [Validators.required]),
      placeOfBirth : this.fb.control(null, [Validators.required, Validators.maxLength(256), Validators.minLength(2)]),
      email : this.fb.control(null, [Validators.required, Validators.email]),
      name : this.fb.control(null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      cin : this.fb.control(null, [Validators.required, Validators.maxLength(256), Validators.minLength(6)]),
      sex : this.fb.control(null, [Validators.required]),
      firstname : this.fb.control( null, [Validators.required, Validators.minLength(2), Validators.maxLength(256)]),
      phone : this.fb.control(null, [Validators.required, Validators.maxLength(16), Validators.minLength(6)])
    });
  }

  updateCustomer() : void {
    let model : CustomerModel = new CustomerModel();
    model.id = this.id;
    model.sex = this.updateCustomerFormGroup.value.sex;
    model.firstname = this.updateCustomerFormGroup.value.firstname;
    model.name = this.updateCustomerFormGroup.value.name;
    model.cin = this.updateCustomerFormGroup.value.cin;
    model.placeOfBirth = this.updateCustomerFormGroup.value.placeOfBirth;
    model.dateOfBirth = this.updateCustomerFormGroup.value.dateOfBirth;
    model.nationality = this.updateCustomerFormGroup.value.nationality;
    model.phone = this.updateCustomerFormGroup.value.phone;
    model.email = this.updateCustomerFormGroup.value.email;
    this.customerService.updateCustomer(this.id, model).subscribe(
      c => {
        this.router.navigate(['show-customer', c.id]).then();
      }
    );
  }

}

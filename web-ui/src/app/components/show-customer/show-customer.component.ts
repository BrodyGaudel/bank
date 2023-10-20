import {Component, OnInit} from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {CustomerModel} from "../../models/customer.model";
import {CustomerService} from "../../services/customer-service/customer.service";
import {AccountService} from "../../services/account-service/account.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AccountModel} from "../../models/account.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-show-customer',
  templateUrl: './show-customer.component.html',
  styleUrls: ['./show-customer.component.css']
})
export class ShowCustomerComponent implements OnInit {

  customerModelObservable!: Observable<CustomerModel>;
  accountModelObservable!: Observable<AccountModel>;
  customerId!: string;
  tmp: number = 1;
  newAccountFormGroup!: FormGroup;
  valid!: boolean;

  constructor(private customerService: CustomerService,
              private accountService: AccountService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.newAccountFormGroup = this.fb.group( {currency : this.fb.control(null, [Validators.required])});
    this.valid = this.newAccountFormGroup.valid;
    this.customerId = this.activatedRoute.snapshot.params['id'];
    this.getCustomerById(this.customerId);
    this.getAccountByCustomerId(this.customerId);
  }

  getCustomerById(id: string): void{
    this.customerModelObservable = this.customerService.getById(id).pipe(
      catchError( (err) => throwError(() => new Error(err.message)))
    );
  }

  getAccountByCustomerId(customerId: string): void{
    this.accountModelObservable = this.accountService.getByCustomerId(customerId).pipe(
      catchError( err => {
        this.tmp = 0;
        return throwError(() => err);
      })
    );
  }

  createAccount(): void{
    let model: AccountModel = new AccountModel();
    model.status = "ACTIVATED";
    model.customerId = this.customerId;
    model.balance = 0;
    model.currency = this.newAccountFormGroup.value.currency;

    this.accountService.save(model).subscribe(
      {
        next : (data: AccountModel) : void =>{
          alert("Account created successfully with ID : "+data.id);
          this.getAccountByCustomerId(data.customerId);
        },
        error : (err) :void =>{
          alert(err);
          console.log(err);
        }
      }
    );
  }

  updateAccountStatus(model: AccountModel): void {
    if(model.status == "ACTIVATED"){
      model.status = "BLOCKED";
    }else{
      model.status = "ACTIVATED";
    }
    this.accountService.updateStatus(model).subscribe(
      {
        next : (data: AccountModel) : void =>{
          this.getAccountByCustomerId(data.customerId);
        },
        error : (err) :void =>{
          alert(err);
          console.log(err);
        }
      }
    );
  }

  changeColorText(text: string) :string {
    if(text == "ACTIVATED"){
      return "text-success";
    }else{
      return "text-danger"
    }
  }
}

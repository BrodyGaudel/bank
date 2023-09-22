import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CustomerService} from "../../services/customer-service/customer.service";
import {catchError, Observable, throwError} from "rxjs";
import {CustomerModel} from "../../models/customer.model";
import {AccountService} from "../../services/account-service/account.service";
import {AccountModel} from "../../models/account.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-show-customer',
  templateUrl: './show-customer.component.html',
  styleUrls: ['./show-customer.component.css']
})
export class ShowCustomerComponent implements OnInit{

  customerId!: string;
  customer!: Observable<CustomerModel>;
  account!: Observable<AccountModel>;
  errorCustomerMessage!: string;
  errorAccountMessage!: string;
  tmp: number = 1;
  tmp2: number = 1;
  addAccountFormGroup!: FormGroup;

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private customerService: CustomerService,
              private accountService: AccountService,
              private fb: FormBuilder) {
  }


  ngOnInit(): void {
    this.initAddAccountFormGroup();
    this.customerId = this.activatedRoute.snapshot.params['id'];
    this.customer = this.getCustomerById(this.customerId);
    this.account = this.getAccountByCustomerId(this.customerId);
  }

  getCustomerById(id: string): Observable<CustomerModel>{
    return this.customerService.getCustomerById(id).pipe(
      catchError(err => {
        this.errorCustomerMessage = err.message;
        return throwError(() => err);
      })
    );
  }

  getAccountByCustomerId(id: string): Observable<AccountModel>{
    return this.accountService.getAccountByCustomerId(id).pipe(
      catchError(err => {
        this.errorAccountMessage = err.message;
        this.tmp = 0;
        return throwError(() => err);
      })
    );
  }


  gotoCreateAccountComponent() : void {
    this.tmp2 = 0;
  }

  initAddAccountFormGroup(): void {
    this.addAccountFormGroup = this.fb.group( {
      currency : this.fb.control(null, [Validators.required]),
    });
  }

  addAccount(): void {
    let model: AccountModel = new AccountModel();
    model.customerId = this.customerId;
    model.currency = this.addAccountFormGroup.value.currency;
    model.balance = 0;
    model.status = 'ACTIVATED';
    this.accountService.saveAccount(model).subscribe({
      next : data  => {
        alert("Bien enregistré ! numéro de compte : "+data.id);
        this.tmp2 = 1;
        this.tmp = 1;
        this.account = this.accountService.getAccountById(data.id);
        },
      error : err => { alert("OUPS ! Problème lors de l'enregistrement"); console.log(err.message) }
    });
  }

  updateAccountStatus(accountModel: AccountModel) : void {
    if(accountModel.status == 'CREATED'){
      accountModel.status = 'ACTIVATED';
    }else if(accountModel.status == 'ACTIVATED'){
      accountModel.status = 'BLOCKED'
    }else if(accountModel.status == 'BLOCKED'){
      accountModel.status = 'ACTIVATED';
    }else{
      alert("ce compte n'a pas de status correct: veuillez contacter l'administrateur");
    }
    this.accountService.updateAccountStatus(accountModel).subscribe({
      next : data  => {
        alert("Status modifié avec succès. Nouveau status : "+data.status);
        this.tmp2 = 1;
        this.tmp = 1;
        this.account = this.accountService.getAccountById(data.id);
      },
      error : err => { alert("OUPS ! Problème lors de la mise à jour du status"); console.log(err.message) }
    });

  }

  gotoUpdateCustomerComponents(id: string): void {
    this.router.navigate(['update-customer', id]).then(
      () : void => {},
      (error) : void => { alert(error.message)}
    );
  }
}

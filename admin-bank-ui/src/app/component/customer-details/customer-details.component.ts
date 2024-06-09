import {Component, OnInit} from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {CustomerResponse} from "../../dto/customer/customer.response";
import {CustomerService} from "../../services/customer/customer.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AccountResponse} from "../../dto/account/account.response";
import {AccountService} from "../../services/account/account.service";
import {UpdateAccountStatusRequest} from "../../dto/account/update-account-status.request";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CreateAccountRequest} from "../../dto/account/create-account.request";

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrl: './customer-details.component.css'
})
export class CustomerDetailsComponent implements OnInit {

  customersObservable!: Observable<CustomerResponse>;
  accountObservable!: Observable<AccountResponse>;
  customerId!: string;
  errorMessage!: string;
  errorDescription!: string;
  errorCode!: number;
  createAccountFormGroup!: FormGroup;

  constructor(private customerService: CustomerService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private accountService: AccountService,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.customerId = this.activatedRoute.snapshot.params['id'];
    this.initializeCreateAccountFormGroup();
    this.customersObservable = this.getCustomerById(this.customerId);
    this.accountObservable = this.getAccountByCustomerId(this.customerId);
  }

  private getCustomerById(id: string):Observable<CustomerResponse> {
    return this.customerService.getCustomerById(id).pipe(
      catchError(err => {
        this.handleError(err);
        return throwError(() => new Error(err.message));
      })
    );
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

  gotoUpdateCustomer(): void {
    this.router.navigate(['/update-customer', this.customerId]).then();
  }

  private getAccountByCustomerId(customerId: string): Observable<AccountResponse> {
    return this.accountService.findByCustomerId(customerId).pipe(
      catchError( (err) => throwError(() => new Error(err.message)))
    );
  }

  gotoAccountDetails(account: AccountResponse): void {
    this.router.navigate(['/account-details', account.id]).then();
  }

  showStatus(status: string) :string {
    if(status == "ACTIVATED"){
      return "Activé"
    }else if(status == "SUSPENDED"){
      return "Suspendu"
    }else if(status == "CREATED"){
      return "Créé";
    }else{
      return "UNKNOWN STATUS"
    }
  }

  statusButton(status: string): string {
    if(status == "ACTIVATED"){
      return "Suspendre";
    }else{
      return "Activer";
    }
  }

  statusButtonStyle(status: string): string {
    if(status == "ACTIVATED"){
      return "btn btn-outline-danger";
    }else{
      return "btn btn-outline-warning";
    }
  }

  updateStatus(account: AccountResponse):void {
    let request: UpdateAccountStatusRequest = new UpdateAccountStatusRequest();
    request.accountId = account.id;
    if(account.status == "ACTIVATED"){
      request.status = "SUSPENDED";
    }else{
      request.status = "ACTIVATED";
    }
    this.accountService.update(request).subscribe({
      next: data => {
        console.log(data);
        alert("Le status du compte a tété modifié avec success !");
        this.accountObservable = this.getAccountById(account.id);
      },
      error : err => {
        console.log(err)
        alert("Il ya eu une erreur lors de la tentative de modification du status ! Si le problème perssiste: veuillez contacter l'administrateur.");
      }
    });
  }

  private initializeCreateAccountFormGroup() :void{
    this.createAccountFormGroup = this.fb.group( {
      currency : this.fb.control(null, [Validators.required, Validators.minLength(3), Validators.maxLength(3)])
    });
  }

  handleCreateAccount() :void{
    let request: CreateAccountRequest = new CreateAccountRequest();
    request.customerId = this.customerId;
    request.currency = this.createAccountFormGroup.value.currency;
    this.accountService.create(request).subscribe({
      next : data  => {
        alert("le compte bacaire a été créer avec le numéro : "+data);
        this.accountObservable = this.getAccountById(data);
      },
      error : err => {
        console.log(err)
        alert("Customer not saved due to : "+err.message);
      }
    });
  }

  private getAccountById(id: string): Observable<AccountResponse> {
    return this.accountService.findById(id).pipe(
      catchError( (err) => throwError(() => new Error(err.message)))
    );
  }
}

import {Component, OnInit} from '@angular/core';
import {Observable, of, tap, throwError} from "rxjs";
import {AccountResponse} from "../../models/response/account.response";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {OperationResponse} from "../../models/response/operation.response";
import {AccountService} from "../../services/accounts/account.service";
import {Router} from "@angular/router";
import {catchError} from "rxjs/operators";
import {CustomerResponse} from "../../models/response/customer.response";
import {CustomerService} from "../../services/customers/customer.service";
import {ErrorHandlerService} from "../../services/exception/error-handler.service";
import {OperationRequest} from "../../models/request/operation.request";

@Component({
  selector: 'app-account-get',
  templateUrl: './account-get.component.html',
  styleUrl: './account-get.component.css'
})
export class AccountGetComponent implements OnInit {

  searchAccountFormGroup!: FormGroup;
  accountObservable!: Observable<AccountResponse | null>;
  customerObservable!: Observable<CustomerResponse | null>;
  operationsObservable!: Observable<Array<OperationResponse> | null>;
  accountId!: string;
  errorMessage: string = '';
  errorDescription: string = '';
  errorCode: number = 0;
  validationErrors: Set<string> = new Set<string>();
  errors: Map<string, string> = new Map<string, string>();
  errorFlag: boolean = false;
  page: number = 0;
  size: number = 3;
  operationFromGroup!: FormGroup;
  showDetails: boolean = false;
  operationDetails: OperationResponse = new OperationResponse();


  constructor(private accountService: AccountService,
              private customerService: CustomerService,
              private fb: FormBuilder,
              private errorHandlerService: ErrorHandlerService,
              private router: Router) {

  }

  ngOnInit(): void {
    this.searchAccountFormGroup = this.fb.group({
      accountId: this.fb.control(null, [Validators.required, Validators.minLength(6), Validators.maxLength(16)])
    });
    this.initOperationFormGroup();
    this.subscribeToErrors();
  }

  private subscribeToErrors(): void {
    this.errorHandlerService.error$.subscribe(error => {
      if (error) {
        this.errorDescription = error.description;
        this.errorCode = error.code;
        this.validationErrors = error.validationErrors;
        this.errorMessage = error.message;
        this.errors = error.errors;
        this.errorFlag = true;
      } else {
        this.errorFlag = false;
      }
    });
  }

  private initOperationFormGroup(): void {
    this.operationFromGroup=this.fb.group({
      type: this.fb.control(null),
      amount: this.fb.control(0),
      description: this.fb.control(null),
      accountDestination: this.fb.control(null)
    });

  }

  private fetchAccountDetails(id: string): void {
    this.accountObservable = this.accountService.getAccountById(id).pipe(
      tap(response => {
        this.errorFlag = false;
        this.fetchCustomerDetails(response.customerId);
        this.fetchAllOperationsDetails(response.id, this.page, this.size);
      }),
      catchError(error => {
        this.errorHandlerService.handleError(error);
        return of(null); // Return a safe value or handle the error appropriately
      })
    );
  }

  handleSearchAccount(): void {
    this.accountId = this.searchAccountFormGroup.value.accountId;
    this.fetchAccountDetails(this.accountId);
  }

  private fetchCustomerDetails(customerId: string): void {
    this.customerObservable = this.customerService.getCustomerById(customerId).pipe(
      tap(response => {
        this.errorFlag = false;
        console.log(response.id);
      }),
      catchError(error => {
        this.errorHandlerService.handleError(error);
        return of(null); // Return a safe value or handle the error appropriately
      })
    );
  }

  private fetchAllOperationsDetails(id: string, pageNumber: number, sizePage:number): void {
    this.operationsObservable = this.accountService.getOperationByAccountId(id,pageNumber,sizePage).pipe(
      tap(response => {
        this.errorFlag = false;
        console.log(response.length);
      }),
      catchError(error => {
        this.errorHandlerService.handleError(error);
        return of(null); // Return a safe value or handle the error appropriately
      })
    );
  }

  gotoNextPage(operations: OperationResponse[]): void {
    if(operations.length >= this.size){
      this.page = this.page + 1;
      this.fetchAllOperationsDetails(this.accountId, this.page, this.size);
    }else{
      alert("C'est la dernière page.");
    }
  }

  gotoPreviousPage(): void {
    if(this.page>0){
      this.page = this.page - 1;
      this.fetchAllOperationsDetails(this.accountId, this.page, this.size);
    }else{
      alert("c'est la prémière page");
    }
  }

  gotoOperationDetailComponent(operation: OperationResponse): void {
    this.operationDetails = operation;
    this.showDetails = true;
  }

  handleAccountOperation(): void {
    let id :string = this.accountId;
    let operationType=this.operationFromGroup.value.type;
    let amount :number =this.operationFromGroup.value.amount;
    let description :string =this.operationFromGroup.value.description;
    if(operationType=='DEBIT'){
      this.debitAccount(id,description, amount);
    }else if(operationType=='CREDIT'){
      this.creditAccount(id,description, amount);
    }
  }

  private debitAccount(id: string, description: string, amount: number): void {
    let request: OperationRequest = new OperationRequest();
    request.accountId = id;
    request.description = description;
    request.amount = amount;
    this.accountService.debitAccount(request).subscribe({
      next : (data)=>{
        alert("Account successfully debited !");
        this.operationFromGroup.reset();
        console.log(data);
        this.handleSearchAccount();
      },
      error : (err)=>{
        console.log(err);
        this.operationFromGroup.reset();
        this.errorHandlerService.handleError(err);
        this.handleSearchAccount();
      }
    });
  }

  private creditAccount(id: string, description: string, amount: number): void {
    let request: OperationRequest = new OperationRequest();
    request.description = description;
    request.accountId = id;
    request.amount = amount;
    this.accountService.creditAccount(request).subscribe({
      next : (data)=>{
        alert("Account successfully credited !");
        this.operationFromGroup.reset();
        console.log(data);
        this.handleSearchAccount();
      },
      error : (err)=>{
        console.log(err);
        this.operationFromGroup.reset();
        this.errorHandlerService.handleError(err);
        this.handleSearchAccount();
      }
    });
  }

  closeDetails() :void {
    this.showDetails = false;
  }
}

import {Component, OnInit} from '@angular/core';
import {OperationService} from "../../services/operation-service/operation.service";
import {CustomerService} from "../../services/customer-service/customer.service";
import {AccountService} from "../../services/account-service/account.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {catchError, Observable, throwError} from "rxjs";
import {HistoryModel} from "../../models/history.model";
import {OperationModel} from "../../models/operation.model";
import {AccountModel} from "../../models/account.model";
import {CustomerModel} from "../../models/customer.model";
import {DebitModel} from "../../models/debit.model";
import {CreditModel} from "../../models/credit.model";
import {AuthService} from "../../services/auth-service/auth.service";

@Component({
  selector: 'app-account-operation',
  templateUrl: './account-operation.component.html',
  styleUrls: ['./account-operation.component.css']
})
export class AccountOperationComponent implements OnInit{

  accountFormGroup!: FormGroup;
  currentPage: number =0;
  pageSize: number = 5;
  historyModelObservable!: Observable<HistoryModel>
  operationFromGroup!: FormGroup;
  errorMessage!: string;
  operation!: Observable<OperationModel>;
  customer!: Observable<CustomerModel>;
  buttonText: string = 'Client';
  buttonStyle: string = 'btn btn-success';
  tmp: number = 0;
  tmp2: number = 0;


  constructor(private operationService: OperationService,
              private customerService: CustomerService,
              private accountService: AccountService,
              private fb: FormBuilder,
              private authService: AuthService) {
  }
  ngOnInit(): void {
    this.authService.security();
    this.accountFormGroup=this.fb.group({
      accountId : this.fb.control('')
    });
    this.operationFromGroup=this.fb.group({
      operationType : this.fb.control(null),
      amount : this.fb.control(0),
      description : this.fb.control(null),
      accountDestination : this.fb.control(null)
    });
  }

  handleSearchAccount() : void {
    let accountId: string = this.accountFormGroup.value.accountId;
    this.historyModelObservable = this.operationService.getHistory(accountId,this.pageSize, this.currentPage)
      .pipe(
        catchError(err => {
          this.errorMessage = err.message;
          return throwError(() => err);
        })
      );
    this.tmp2 = 0;
  }

  gotoPage(page: number) : void {
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleAccountOperation(): void {
    let operationType = this.operationFromGroup.value.operationType;
    if(operationType=='DEBIT'){
      this.debitAccount();
    }else if (operationType=='CREDIT'){
      this.creditAccount();
    }
  }

  debitAccount(): void {
    let model: DebitModel = new DebitModel();
    model.id = this.accountFormGroup.value.accountId;
    model.amount = this.operationFromGroup.value.amount;
    model.description = this.operationFromGroup.value.description;
    this.operationService.debitAccount(model).subscribe(
      {
        next : (data: DebitModel) : void =>{
          alert("Success Debit: "+data.amount);
          this.operationFromGroup.reset();
          this.handleSearchAccount();
        },
        error : (err) : void =>{
          alert(err.message);
          console.log(err);
        }
      }
    );
  }

  creditAccount(): void {
    let model: CreditModel = new CreditModel();
    model.id = this.accountFormGroup.value.accountId;
    model.amount = this.operationFromGroup.value.amount;
    model.description = this.operationFromGroup.value.description;
    this.operationService.creditAccount(model).subscribe(
      {
        next : (data: CreditModel) : void =>{
          alert("Success Credit: "+data.amount);
          this.operationFromGroup.reset();
          this.handleSearchAccount();
        },
        error : (err) : void =>{
          alert(err.message);
          console.log(err);
        }
      }
    );
  }


  showCustomerInformation(customerId: string) : void {
    if(this.tmp == 0){
      this.buttonStyle = 'btn btn-danger';
      this.buttonText = 'Fermer'
      this.getCustomerById(customerId);
      this.tmp = 1;
      this.buttonStyle = 'btn btn-danger';
      this.buttonText = 'Fermer';
    }else{
      this.tmp = 0;
      this.buttonStyle = 'btn btn-success';
      this.buttonText = 'Client';
    }
  }

  getCustomerById(id: string): void{
    this.accountService.getAccountById(id).subscribe(
      {
        next : (data: AccountModel) : void =>{
          this.customer = this.customerService.getCustomerById(data.customerId).pipe(
            catchError(err => {
              this.errorMessage = err.message;
              return throwError(() => err);
            })
          );
        },
        error : (err) : void =>{
          alert(err.message);
          console.log(err);
        }
      }
    );
  }

  getOperationById(id: string): void{
    this.operation = this.operationService.getOperationById(id).pipe(
      catchError(err => {
        console.log(err.message);
        return throwError(() => err);
      })
    );
  }

  getOperationInformation(id: string) : void {
    if(this.tmp2 == 0){
      this.getOperationById(id);
      this.tmp2 = 1;
    }else {
      this.tmp2 = 0;
    }
  }

  closeOperationInformation() : void {
    this.tmp2 = 0;
  }

}
